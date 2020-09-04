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
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailOut;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.pec.InviaMailAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.InviaMailActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class InviaMailActionActionHandler implements ActionHandler<InviaMailAction, InviaMailActionResult> {

	@Autowired
	XMLPluginToDTOConverter xmlPluginToDTOConverter;

	@Autowired
	PraticaSessionUtil praticaSessionUtil;

	@Autowired
	GestioneEmailOutClient inviaBozzaClient;

	@Autowired
	UserSessionUtil userSessionUtil;

	private static final Logger logger = LoggerFactory.getLogger(InviaMailActionActionHandler.class);

	public InviaMailActionActionHandler() {

	}

	@Override
	public InviaMailActionResult execute(InviaMailAction action, ExecutionContext context) throws ActionException {

		String path = Base64Utils.URLdecodeAlfrescoPath(action.getClientID());
		logger.info("Inizio esecuzione servizio di invio email. Path bozza: {}", path);
		InviaMailActionResult inviaMailActionResult = new InviaMailActionResult("", false);

		try {
			LockedPratica lockedPratica = inviaBozzaClient.inviaBozza(path, userSessionUtil.getUtenteSpagic());
			Pratica<?> pratica = praticaSessionUtil.loadPraticaInSessione(lockedPratica);
			PecOutDTO pecOutDTO = xmlPluginToDTOConverter.emailToDettaglioOUT((PraticaEmailOut) pratica);
			praticaSessionUtil.removePraticaFromEncodedPath(pecOutDTO.getClientID());
			inviaMailActionResult.setPecOuDTO(pecOutDTO);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			inviaMailActionResult.setError(true);
			inviaMailActionResult.setMessError(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			inviaMailActionResult.setError(true);
			inviaMailActionResult.setMessError(ConsolePecConstants.ERROR_MESSAGE);
		}

		logger.info("Fine esecuzione servizio di invio email. Path bozza: {}", path);
		return inviaMailActionResult;
	}

	@Override
	public void undo(InviaMailAction action, InviaMailActionResult result, ExecutionContext context) throws ActionException {

	}

	@Override
	public Class<InviaMailAction> getActionType() {
		return InviaMailAction.class;
	}
}
