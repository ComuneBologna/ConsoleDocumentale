package it.eng.portlet.consolepec.gwt.servermock.pec;

import it.eng.portlet.consolepec.gwt.servermock.PecInDB;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CaricaPraticaEmailInAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CaricaPraticaEmailInActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class CaricaPraticaEmailMockActionHandler implements ActionHandler<CaricaPraticaEmailInAction, CaricaPraticaEmailInActionResult> {

	Logger logger = LoggerFactory.getLogger(CaricaPraticaEmailMockActionHandler.class);

	public CaricaPraticaEmailMockActionHandler() {
	}

	@Override
	public CaricaPraticaEmailInActionResult execute(CaricaPraticaEmailInAction action, ExecutionContext context) throws ActionException {
		CaricaPraticaEmailInActionResult res = new CaricaPraticaEmailInActionResult();
		PecInDTO dett = PecInDB.getInstance().getDettaglio(action.getClientID());
		res.setDettaglio(dett);
		return res;

	}

	@Override
	public void undo(CaricaPraticaEmailInAction action, CaricaPraticaEmailInActionResult result, ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<CaricaPraticaEmailInAction> getActionType() {
		return CaricaPraticaEmailInAction.class;
	}
}
