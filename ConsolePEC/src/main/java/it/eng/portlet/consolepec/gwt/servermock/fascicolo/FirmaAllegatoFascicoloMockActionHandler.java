package it.eng.portlet.consolepec.gwt.servermock.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.FirmaAllegatoFascicoloAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.FirmaAllegatoFascicoloResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class FirmaAllegatoFascicoloMockActionHandler implements ActionHandler<FirmaAllegatoFascicoloAction, FirmaAllegatoFascicoloResult> {

	public FirmaAllegatoFascicoloMockActionHandler() {
	}

	@Override
	public FirmaAllegatoFascicoloResult execute(FirmaAllegatoFascicoloAction action, ExecutionContext context) throws ActionException {
		FascicoloDB db = FascicoloDB.getInstance();
		FascicoloDTO res = db.getDettaglio(action.getClientID());
		
		FirmaAllegatoFascicoloResult result = new FirmaAllegatoFascicoloResult(res, null, false);
		return result;
	}

	@Override
	public void undo(FirmaAllegatoFascicoloAction action, FirmaAllegatoFascicoloResult result, ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<FirmaAllegatoFascicoloAction> getActionType() {
		return FirmaAllegatoFascicoloAction.class;
	}
}
