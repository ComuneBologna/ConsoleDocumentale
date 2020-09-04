package it.eng.portlet.consolepec.gwt.server.pec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.client.GestioneEmailOutClient;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.pec.EliminaBozza;
import it.eng.portlet.consolepec.gwt.shared.action.pec.EliminaBozzaResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class EliminaBozzaActionHandler implements ActionHandler<EliminaBozza, EliminaBozzaResult> {

	@Autowired
	GestioneEmailOutClient gestioneEmailOutClient;

	@Autowired
	XMLPluginToDTOConverter utilPratica;

	@Autowired
	PraticaSessionUtil praticaSessionUtil;

	@Autowired
	UserSessionUtil userSessionUtil;

	private static final Logger logger = LoggerFactory.getLogger(EliminaBozzaActionHandler.class);

	public EliminaBozzaActionHandler() {}

	@Override
	public EliminaBozzaResult execute(EliminaBozza action, ExecutionContext context) throws ActionException {
		try {
			String pathPratica = Base64Utils.URLdecodeAlfrescoPath(action.getClientID());
			String pathFascicolo = Base64Utils.URLdecodeAlfrescoPath(action.getClientIDFascicolo());
			LockedPratica deleteDraftMail = gestioneEmailOutClient.eliminaBozza(pathPratica, pathFascicolo, userSessionUtil.getUtenteSpagic());
			Fascicolo fascicolo = (Fascicolo) praticaSessionUtil.loadPraticaInSessione(deleteDraftMail);
			FascicoloDTO dto = utilPratica.fascicoloToDettaglio(fascicolo);
			return new EliminaBozzaResult(dto, null, false);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			return new EliminaBozzaResult(null, e.getErrorMessage(), true);

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			return new EliminaBozzaResult(null, ConsolePecConstants.ERROR_MESSAGE, true);
		}

	}

	@Override
	public void undo(EliminaBozza action, EliminaBozzaResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<EliminaBozza> getActionType() {
		return EliminaBozza.class;
	}
}
