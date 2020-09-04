
package it.eng.portlet.consolepec.gwt.servermock.pec;

import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CreaFascicoloAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CreaFascicoloActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class CreaFascicoloMockActionHandler
	implements ActionHandler<CreaFascicoloAction, CreaFascicoloActionResult> {

	public CreaFascicoloMockActionHandler() {

	}

	@Override
	public CreaFascicoloActionResult execute(
		CreaFascicoloAction action, ExecutionContext context)
		throws ActionException {

		CreaFascicoloActionResult actionResult =
			new CreaFascicoloActionResult();

		FascicoloDTO fascicoloDTO = new FascicoloDTO("//");
		actionResult.setFascicoloDTO(fascicoloDTO);

		return actionResult;
	}

	@Override
	public void undo(
		CreaFascicoloAction action, CreaFascicoloActionResult result,
		ExecutionContext context)
		throws ActionException {

	}

	@Override
	public Class<CreaFascicoloAction> getActionType() {

		return CreaFascicoloAction.class;
	}
}
