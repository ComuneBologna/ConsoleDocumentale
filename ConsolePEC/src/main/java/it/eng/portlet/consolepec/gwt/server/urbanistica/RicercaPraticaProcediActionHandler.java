package it.eng.portlet.consolepec.gwt.server.urbanistica;

import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.services.PraticaProcediResponse;
import it.eng.cobo.consolepec.commons.services.ServiceResponse;
import it.eng.consolepec.client.IntegrazionePraticaProcediClient;
import it.eng.portlet.consolepec.gwt.shared.action.urbanistica.RicercaPraticaProcediAction;
import it.eng.portlet.consolepec.gwt.shared.action.urbanistica.RicercaPraticaProcediResult;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

/**
 * @author GiacomoFM
 * @since 06/nov/2017
 */
public class RicercaPraticaProcediActionHandler implements ActionHandler<RicercaPraticaProcediAction, RicercaPraticaProcediResult> {

	@Autowired
	private IntegrazionePraticaProcediClient integrazionePraticaProcediClient;
	@Autowired
	private UserSessionUtil userSessionUtil;

	@Override
	public RicercaPraticaProcediResult execute(RicercaPraticaProcediAction action, ExecutionContext arg1) throws ActionException {
		ServiceResponse<PraticaProcediResponse> serviceResponse = integrazionePraticaProcediClient.ricerca(action.getFiltri(), action.getOrdinamento(), action.getLimit(), action.getOffset(),
				userSessionUtil.getUtenteSpagic());

		if (serviceResponse.isError()) {
			return new RicercaPraticaProcediResult(serviceResponse.getServiceError().getMessage());
		}

		ServiceResponse<PraticaProcediResponse> serviceResponseCount = integrazionePraticaProcediClient.countPratiche(action.getFiltri(), userSessionUtil.getUtenteSpagic());

		return new RicercaPraticaProcediResult(serviceResponse.getResponse().getListaPraticaProcedi(), serviceResponseCount.getResponse().getCount());
	}

	@Override
	public Class<RicercaPraticaProcediAction> getActionType() {
		return RicercaPraticaProcediAction.class;
	}

	@Override
	public void undo(RicercaPraticaProcediAction arg0, RicercaPraticaProcediResult arg1, ExecutionContext arg2) throws ActionException {
		//
	}

}
