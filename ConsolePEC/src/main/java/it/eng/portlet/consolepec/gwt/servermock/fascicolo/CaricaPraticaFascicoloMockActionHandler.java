package it.eng.portlet.consolepec.gwt.servermock.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CaricaPraticaFascicoloAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CaricaPraticaFascicoloActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class CaricaPraticaFascicoloMockActionHandler implements ActionHandler<CaricaPraticaFascicoloAction, CaricaPraticaFascicoloActionResult> {

	public CaricaPraticaFascicoloMockActionHandler() {
	}

	@Override
	public CaricaPraticaFascicoloActionResult execute(CaricaPraticaFascicoloAction action, ExecutionContext context) throws ActionException {
		
		FascicoloDTO dto = FascicoloDB.getInstance().getDettaglio(action.getClientID());		
		CaricaPraticaFascicoloActionResult result = new CaricaPraticaFascicoloActionResult(null, false, dto);
		
		return result;
	}

	@Override
	public void undo(CaricaPraticaFascicoloAction action, CaricaPraticaFascicoloActionResult result, ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<CaricaPraticaFascicoloAction> getActionType() {
		return CaricaPraticaFascicoloAction.class;
	}
}
