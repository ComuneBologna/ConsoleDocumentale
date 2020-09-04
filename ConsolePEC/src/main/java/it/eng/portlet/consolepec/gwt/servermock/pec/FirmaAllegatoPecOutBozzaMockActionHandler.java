package it.eng.portlet.consolepec.gwt.servermock.pec;

import it.eng.portlet.consolepec.gwt.servermock.PecOutDB;
import it.eng.portlet.consolepec.gwt.shared.action.FirmaAllegatoPecOutBozzaAction;
import it.eng.portlet.consolepec.gwt.shared.action.FirmaAllegatoPecOutBozzaActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class FirmaAllegatoPecOutBozzaMockActionHandler implements ActionHandler<FirmaAllegatoPecOutBozzaAction, FirmaAllegatoPecOutBozzaActionResult> {

	public FirmaAllegatoPecOutBozzaMockActionHandler() {

	}

	@Override
	public FirmaAllegatoPecOutBozzaActionResult execute(FirmaAllegatoPecOutBozzaAction action, ExecutionContext context) throws ActionException {

		PecOutDTO pecout = PecOutDB.getInstance().getDettaglio(action.getClientID());
		FirmaAllegatoPecOutBozzaActionResult res = new FirmaAllegatoPecOutBozzaActionResult(pecout, null, false);
		return res;
	}

	@Override
	public void undo(FirmaAllegatoPecOutBozzaAction action, FirmaAllegatoPecOutBozzaActionResult result, ExecutionContext context) throws ActionException {

	}

	@Override
	public Class<FirmaAllegatoPecOutBozzaAction> getActionType() {

		return FirmaAllegatoPecOutBozzaAction.class;
	}
}
