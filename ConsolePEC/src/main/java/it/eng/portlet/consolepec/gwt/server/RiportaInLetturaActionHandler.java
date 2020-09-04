package it.eng.portlet.consolepec.gwt.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.SpagicClientRiportaInLettura;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailIn;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.RiportaInLettura;
import it.eng.portlet.consolepec.gwt.shared.action.RiportaInLetturaResult;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

/**
 * Servizio generico di recupero ruoli di riassegnazione e creazione pratica
 *
 */
public class RiportaInLetturaActionHandler implements ActionHandler<RiportaInLettura, RiportaInLetturaResult> {

	private static Logger logger = LoggerFactory.getLogger(RiportaInLetturaActionHandler.class);

	@Autowired
	SpagicClientRiportaInLettura spagicClientRiportaInLettura;
	@Autowired
	PraticaSessionUtil praticaSessionUtil;
	@Autowired
	UserSessionUtil userSessionUtil;
	@Autowired
	XMLPluginToDTOConverter util;

	public RiportaInLetturaActionHandler() {}

	@Override
	public Class<RiportaInLettura> getActionType() {
		return RiportaInLettura.class;
	}

	@Override
	public RiportaInLetturaResult execute(RiportaInLettura action, ExecutionContext context) throws ActionException {
		try {
			Pratica<?> pratica = praticaSessionUtil.loadPraticaFromEncodedPath(action.getClientID());
			LockedPratica praticaLock = spagicClientRiportaInLettura.riportaInLettura(pratica.getAlfrescoPath(), userSessionUtil.getUtenteSpagic());
			pratica = praticaSessionUtil.loadPraticaInSessione(praticaLock);

			praticaSessionUtil.removePraticaFromEncodedPath(action.getClientID());

			if (pratica instanceof Fascicolo) {
				return new RiportaInLetturaResult(util.fascicoloToDettaglio((Fascicolo) pratica));
			}
			if (pratica instanceof PraticaEmailIn) {
				return new RiportaInLetturaResult(util.emailToDettaglioIN((PraticaEmailIn) pratica));
			}
			logger.error("Tipo pratica non gestito come output");
			return new RiportaInLetturaResult("");

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			return new RiportaInLetturaResult(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			return new RiportaInLetturaResult(ConsolePecConstants.ERROR_MESSAGE);

		}

	}

	@Override
	public void undo(RiportaInLettura action, RiportaInLetturaResult result, ExecutionContext context) throws ActionException {

	}

}
