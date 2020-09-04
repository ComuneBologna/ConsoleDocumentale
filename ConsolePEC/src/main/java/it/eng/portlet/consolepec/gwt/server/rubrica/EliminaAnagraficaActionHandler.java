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
import it.eng.portlet.consolepec.gwt.shared.action.rubrica.EliminaAnagraficaAction;
import it.eng.portlet.consolepec.gwt.shared.action.rubrica.EliminaAnagraficaResult;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

/**
 * @author GiacomoFM
 * @since 27/ott/2017
 */
public class EliminaAnagraficaActionHandler implements ActionHandler<EliminaAnagraficaAction, EliminaAnagraficaResult> {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RubricaClient rubricaClient;
	@Autowired
	private UserSessionUtil userSessionUtil;

	@Override
	public EliminaAnagraficaResult execute(EliminaAnagraficaAction action, ExecutionContext arg1) throws ActionException {
		try {
			ServiceResponse<AnagraficaResponse> serviceResponse = rubricaClient.elimina(action.getAnagrafica(), userSessionUtil.getUtenteSpagic());

			if (serviceResponse.isError()) {
				return new EliminaAnagraficaResult(serviceResponse.getServiceError().getMessage());

			} else {
				return new EliminaAnagraficaResult();
			}

		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
			return new EliminaAnagraficaResult(ConsolePecConstants.ERROR_MESSAGE);
		}
	}

	@Override
	public Class<EliminaAnagraficaAction> getActionType() {
		return EliminaAnagraficaAction.class;
	}

	@Override
	public void undo(EliminaAnagraficaAction arg0, EliminaAnagraficaResult arg1, ExecutionContext arg2) throws ActionException {
		// TODO Auto-generated method stub

	}

}
