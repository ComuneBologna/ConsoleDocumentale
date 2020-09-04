package it.eng.portlet.consolepec.gwt.servermock.pec;

import it.eng.portlet.consolepec.gwt.servermock.PecOutDB;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CaricaPraticaEmailOutAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CaricaPraticaEmailOutActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class CaricaPraticaEmailOutMockActionHandler implements ActionHandler<CaricaPraticaEmailOutAction, CaricaPraticaEmailOutActionResult> {

	public CaricaPraticaEmailOutMockActionHandler() {
	}

	@Override
	public CaricaPraticaEmailOutActionResult execute(CaricaPraticaEmailOutAction action, ExecutionContext context) throws ActionException {
		PecOutDTO dett = PecOutDB.getInstance().getDettaglio(action.getClientId());
		CaricaPraticaEmailOutActionResult res = new CaricaPraticaEmailOutActionResult(dett);
		return res;
	}

	@Override
	public void undo(CaricaPraticaEmailOutAction action, CaricaPraticaEmailOutActionResult result, ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<CaricaPraticaEmailOutAction> getActionType() {
		return CaricaPraticaEmailOutAction.class;
	}
}
