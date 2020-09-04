package it.eng.portlet.consolepec.gwt.server.fascicolo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.client.AssegnazioneEsternaClient;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.AssegnaUtenteEsternoAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.AssegnaUtenteEsternoResult;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class AssegnaUtenteEsternoActionHandler implements ActionHandler<AssegnaUtenteEsternoAction, AssegnaUtenteEsternoResult> {

	@Autowired
	UserSessionUtil userSessionUtil;

	@Autowired
	PraticaSessionUtil praticaSessionUtil;

	@Autowired
	AssegnazioneEsternaClient assegnazioneEsternaClient;

	private static final Logger logger = LoggerFactory.getLogger(AssegnaUtenteEsternoActionHandler.class);

	public AssegnaUtenteEsternoActionHandler() {}

	@Override
	public AssegnaUtenteEsternoResult execute(AssegnaUtenteEsternoAction action, ExecutionContext context) throws ActionException {

		logger.debug("Action handler per l'assegnazione dell'utente esterno");

		String praticaPath = Base64Utils.URLdecodeAlfrescoPath(action.getPraticaPath()); // devo passare il path del fasciolo in chiaro

		try {
			assegnazioneEsternaClient.assegna(praticaPath, action.getDestinatari(), action.getTestoEmail(), action.getOperazioni(), userSessionUtil.getUtenteSpagic());
			praticaSessionUtil.removePraticaFromEncodedPath(action.getPraticaPath());

			return new AssegnaUtenteEsternoResult();

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			return new AssegnaUtenteEsternoResult(e.getErrorMessage(), true);

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			return new AssegnaUtenteEsternoResult(ConsolePecConstants.ERROR_MESSAGE, true);

		}

	}

	@Override
	public void undo(AssegnaUtenteEsternoAction action, AssegnaUtenteEsternoResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<AssegnaUtenteEsternoAction> getActionType() {
		return AssegnaUtenteEsternoAction.class;
	}
}
