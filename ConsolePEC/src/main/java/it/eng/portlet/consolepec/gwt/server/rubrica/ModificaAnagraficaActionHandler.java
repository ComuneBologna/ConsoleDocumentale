package it.eng.portlet.consolepec.gwt.server.rubrica;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.services.AnagraficaResponse;
import it.eng.cobo.consolepec.commons.services.ServiceResponse;
import it.eng.consolepec.client.RubricaClient;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.rubrica.ModificaAnagraficaAction;
import it.eng.portlet.consolepec.gwt.shared.action.rubrica.ModificaAnagraficaResult;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

/**
 * @author GiacomoFM
 * @since 17/ott/2017
 */
public class ModificaAnagraficaActionHandler implements ActionHandler<ModificaAnagraficaAction, ModificaAnagraficaResult> {

	private Logger logger = LoggerFactory.getLogger(ModificaAnagraficaActionHandler.class);

	@Autowired
	private RubricaClient rubricaClient;
	@Autowired
	private UserSessionUtil userSessionUtil;

	@Override
	public ModificaAnagraficaResult execute(ModificaAnagraficaAction action, ExecutionContext arg1) throws ActionException {

		try {
			ServiceResponse<AnagraficaResponse> serviceResponse = rubricaClient.modifica(action.getAnagrafica(), userSessionUtil.getUtenteSpagic());

			if (serviceResponse.isError()) {
				return new ModificaAnagraficaResult(serviceResponse.getServiceError().getMessage());

			} else {
				return new ModificaAnagraficaResult(serviceResponse.getResponse().getAnagrafica());
			}

		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
			return new ModificaAnagraficaResult(ConsolePecConstants.ERROR_MESSAGE);
		}
	}

	@Override
	public Class<ModificaAnagraficaAction> getActionType() {
		return ModificaAnagraficaAction.class;
	}

	@Override
	public void undo(ModificaAnagraficaAction arg0, ModificaAnagraficaResult arg1, ExecutionContext arg2) throws ActionException {
		//
	}

}
