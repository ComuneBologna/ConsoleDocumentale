package it.eng.portlet.consolepec.gwt.server.template.eliminazione;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.SpagicClientGestioneTemplate;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.template.eliminazione.EliminaTemplateAction;
import it.eng.portlet.consolepec.gwt.shared.action.template.eliminazione.EliminaTemplateResult;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

/**
 *
 * @author biagiot
 *
 */
public class EliminaTemplateActionHandler implements ActionHandler<EliminaTemplateAction, EliminaTemplateResult> {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	SpagicClientGestioneTemplate spagicClientGestioneTemplate;
	@Autowired
	UserSessionUtil userSessionUtil;

	public EliminaTemplateActionHandler() {

	}

	@Override
	public EliminaTemplateResult execute(EliminaTemplateAction action, ExecutionContext context) throws ActionException {
		logger.info("Start EliminaTemplateMailActionHandler");
		boolean esito = true;
		String message = "OK";

		try {

			if (Strings.isNullOrEmpty(action.getPathTemplate())) {
				esito = false;
				message = ConsolePecConstants.ERROR_MESSAGE;

			} else {
				String decPath = Base64Utils.URLdecodeAlfrescoPath(action.getPathTemplate());
				esito = spagicClientGestioneTemplate.elimina(decPath, userSessionUtil.getUtenteSpagic());
				if (!esito) {
					message = ConsolePecConstants.ERROR_MESSAGE;
				}
			}

			logger.info("End EliminaTemplateMailActionHandler");
			return new EliminaTemplateResult(esito, message);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			return new EliminaTemplateResult(false, e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			return new EliminaTemplateResult(false, ConsolePecConstants.ERROR_MESSAGE);
		}

	}

	@Override
	public Class<EliminaTemplateAction> getActionType() {
		return EliminaTemplateAction.class;
	}

	@Override
	public void undo(EliminaTemplateAction arg0, EliminaTemplateResult arg1, ExecutionContext arg2) throws ActionException {}
}
