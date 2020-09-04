package it.eng.portlet.consolepec.gwt.server.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.SpagicClientGestioneTemplate;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.template.CreaPdfDaTemplateAction;
import it.eng.portlet.consolepec.gwt.shared.action.template.CreaPdfDaTemplateActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class CreaPdfDaTemplateActionHandler extends CreaGenericoDaTemplateActionHandler<CreaPdfDaTemplateAction, CreaPdfDaTemplateActionResult> {

	private Logger logger = LoggerFactory.getLogger(CreaPdfDaTemplateActionHandler.class);

	@Autowired
	SpagicClientGestioneTemplate spagicClientGestioneTemplate;
	@Autowired
	UserSessionUtil userSessionUtil;

	@Override
	protected CreaPdfDaTemplateActionResult getResult(CreaPdfDaTemplateAction action) throws ActionException {

		try {
			String pathFascicolo = decodePathFascicolo(action);
			String pathTemplate = decodePathTemplate(action);

			logger.debug("Creazione PDF da Template. fascicolo:" + pathFascicolo + " - template " + pathTemplate);

			LockedPratica lockedPratica = spagicClientGestioneTemplate.creaPDF(pathFascicolo, pathTemplate, action.getValori(), action.getNomeFile(), userSessionUtil.getUtenteSpagic());
			FascicoloDTO fascicoloDTO = ricaricaFascicolo(lockedPratica);

			return new CreaPdfDaTemplateActionResult(fascicoloDTO);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			return new CreaPdfDaTemplateActionResult(true, e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			return new CreaPdfDaTemplateActionResult(true, ConsolePecConstants.ERROR_MESSAGE);

		} finally {
			logger.debug("Fine salva template");
		}
	}

	@Override
	public Class<CreaPdfDaTemplateAction> getActionType() {
		return CreaPdfDaTemplateAction.class;
	}

	@Override
	public void undo(CreaPdfDaTemplateAction action, CreaPdfDaTemplateActionResult result, ExecutionContext paramExecutionContext) throws ActionException {}

}
