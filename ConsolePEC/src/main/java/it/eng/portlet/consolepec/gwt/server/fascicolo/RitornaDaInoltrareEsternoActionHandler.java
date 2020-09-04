package it.eng.portlet.consolepec.gwt.server.fascicolo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.client.AssegnazioneEsternaClient;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.RitornaDaInoltrareEsternoAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.RitornaDaInoltrareEsternoResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class RitornaDaInoltrareEsternoActionHandler implements ActionHandler<RitornaDaInoltrareEsternoAction, RitornaDaInoltrareEsternoResult> {

	private static final Logger logger = LoggerFactory.getLogger(RitornaDaInoltrareEsternoActionHandler.class);

	@Autowired
	AssegnazioneEsternaClient assegnazioneEsternaClient;

	@Autowired
	PraticaSessionUtil praticaSessionUtil;

	@Autowired
	UserSessionUtil userSessionUtil;

	@Autowired
	XMLPluginToDTOConverter utilPratica;

	public RitornaDaInoltrareEsternoActionHandler() {}

	@Override
	public Class<RitornaDaInoltrareEsternoAction> getActionType() {
		return RitornaDaInoltrareEsternoAction.class;
	}

	@Override
	public RitornaDaInoltrareEsternoResult execute(RitornaDaInoltrareEsternoAction action, ExecutionContext context) throws ActionException {
		try {

			logger.debug("RitornaDaInoltrareEsternoActionHandler inizio");

			String praticaPath = Base64Utils.URLdecodeAlfrescoPath(action.getClientID());
			LockedPratica lp = assegnazioneEsternaClient.concludiAssegnazioneEsterna(praticaPath, userSessionUtil.getUtenteSpagic());

			Fascicolo fascicolo = (Fascicolo) praticaSessionUtil.loadPraticaInSessione(lp);
			FascicoloDTO dto = utilPratica.fascicoloToDettaglio(fascicolo);
			praticaSessionUtil.removePraticaFromEncodedPath(action.getClientID());

			return new RitornaDaInoltrareEsternoResult(dto);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			return new RitornaDaInoltrareEsternoResult(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			return new RitornaDaInoltrareEsternoResult(ConsolePecConstants.ERROR_MESSAGE);

		}
	}

	@Override
	public void undo(RitornaDaInoltrareEsternoAction action, RitornaDaInoltrareEsternoResult result, ExecutionContext context) throws ActionException {

	}

}
