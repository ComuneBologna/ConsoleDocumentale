package it.eng.portlet.consolepec.gwt.server.urbanistica;

import it.eng.cobo.consolepec.commons.services.PraticaProcediResponse;
import it.eng.cobo.consolepec.commons.services.ServiceResponse;
import it.eng.consolepec.client.IntegrazionePraticaProcediClient;
import it.eng.portlet.consolepec.gwt.shared.action.urbanistica.DettaglioPraticaProcediAction;
import it.eng.portlet.consolepec.gwt.shared.action.urbanistica.DettaglioPraticaProcediResult;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

/**
 * @author GiacomoFM
 * @since 07/nov/2017
 */
public class DettaglioPraticaProcediActionHandler implements ActionHandler<DettaglioPraticaProcediAction, DettaglioPraticaProcediResult> {

	@Autowired private IntegrazionePraticaProcediClient integrazionePraticaProcediClient;
	@Autowired private UserSessionUtil userSessionUtil;

	@Override
	public DettaglioPraticaProcediResult execute(DettaglioPraticaProcediAction action, ExecutionContext arg1) throws ActionException {
		ServiceResponse<PraticaProcediResponse> serviceResponse = integrazionePraticaProcediClient.dettaglio(action.getIds(), userSessionUtil.getUtenteSpagic());
		
		if (serviceResponse.isError()) {
			return new DettaglioPraticaProcediResult(serviceResponse.getServiceError().getMessage());
		}
		
		return new DettaglioPraticaProcediResult(serviceResponse.getResponse().getListaPraticaProcedi());
	}

	@Override
	public Class<DettaglioPraticaProcediAction> getActionType() {
		return DettaglioPraticaProcediAction.class;
	}

	@Override
	public void undo(DettaglioPraticaProcediAction arg0, DettaglioPraticaProcediResult arg1, ExecutionContext arg2) throws ActionException {
		//
	}

}
