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
import it.eng.portlet.consolepec.gwt.shared.action.rubrica.CreaAnagraficaAction;
import it.eng.portlet.consolepec.gwt.shared.action.rubrica.CreaAnagraficaResult;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

/**
 * @author GiacomoFM
 * @since 17/ott/2017
 */
public class CreaAnagraficaActionHandler implements ActionHandler<CreaAnagraficaAction, CreaAnagraficaResult> {

	private Logger logger = LoggerFactory.getLogger(CreaAnagraficaActionHandler.class);

	@Autowired
	private RubricaClient rubricaClient;
	@Autowired
	private UserSessionUtil userSessionUtil;

	@Override
	public CreaAnagraficaResult execute(CreaAnagraficaAction action, ExecutionContext arg1) throws ActionException {

		try {
			ServiceResponse<AnagraficaResponse> serviceResponse = rubricaClient.crea(action.getAnagrafica(), userSessionUtil.getUtenteSpagic());

			if (serviceResponse.isError()) {
				return new CreaAnagraficaResult(serviceResponse.getServiceError().getMessage());

			} else {
				return new CreaAnagraficaResult(serviceResponse.getResponse().getAnagrafica());
			}

		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
			return new CreaAnagraficaResult(ConsolePecConstants.ERROR_MESSAGE);
		}
	}

	@Override
	public Class<CreaAnagraficaAction> getActionType() {
		return CreaAnagraficaAction.class;
	}

	@Override
	public void undo(CreaAnagraficaAction arg0, CreaAnagraficaResult arg1, ExecutionContext arg2) throws ActionException {
		//
	}

}
