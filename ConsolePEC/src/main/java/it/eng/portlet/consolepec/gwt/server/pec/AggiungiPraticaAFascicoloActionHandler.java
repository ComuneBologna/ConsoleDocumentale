package it.eng.portlet.consolepec.gwt.server.pec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.bologna.comune.alfresco.generic.service.Response;
import it.eng.consolepec.spagicclient.SpagicClientGestioneFascicolo;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.pec.AggiungiPraticaAFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.pec.AggiungiPraticaAFascicoloResult;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class AggiungiPraticaAFascicoloActionHandler implements ActionHandler<AggiungiPraticaAFascicolo, AggiungiPraticaAFascicoloResult> {

	@Autowired
	SpagicClientGestioneFascicolo spagicClientGestioneFascicolo;

	@Autowired
	PraticaSessionUtil praticaSessionUtil;

	@Autowired
	private UserSessionUtil userSessionUtil;

	private Logger logger = LoggerFactory.getLogger(AggiungiPraticaAFascicoloActionHandler.class);

	public AggiungiPraticaAFascicoloActionHandler() {

	}

	@Override
	public AggiungiPraticaAFascicoloResult execute(AggiungiPraticaAFascicolo action, ExecutionContext context) throws ActionException {
		String clientIdFascicolo = action.getIdFascicolo();
		String clientIdPratica = action.getIdPratica();
		String pathFascicolo = Base64Utils.URLdecodeAlfrescoPath(clientIdFascicolo);
		String pathPratica = Base64Utils.URLdecodeAlfrescoPath(clientIdPratica);
		try {
			Response aggiungiPratica = spagicClientGestioneFascicolo.aggiungiPratica(pathFascicolo, pathPratica, userSessionUtil.getUtenteSpagic());
			praticaSessionUtil.loadPraticaInSessione(new LockedPratica(aggiungiPratica.getLockedPratica().getHashPratica(), aggiungiPratica.getLockedPratica().getXmlpratica()));
			praticaSessionUtil.loadPraticaInSessione(new LockedPratica(aggiungiPratica.getLockedPratica().getHashFascicolo(), aggiungiPratica.getLockedPratica().getXmlfascicolo()));

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			return new AggiungiPraticaAFascicoloResult(e.getErrorMessage(), true);

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			return new AggiungiPraticaAFascicoloResult(ConsolePecConstants.ERROR_MESSAGE, true);

		}

		return new AggiungiPraticaAFascicoloResult(null, false);
	}

	@Override
	public void undo(AggiungiPraticaAFascicolo action, AggiungiPraticaAFascicoloResult result, ExecutionContext context) throws ActionException {

	}

	@Override
	public Class<AggiungiPraticaAFascicolo> getActionType() {

		return AggiungiPraticaAFascicolo.class;
	}
}
