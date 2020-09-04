package it.eng.portlet.consolepec.gwt.servermock;

import it.eng.portlet.consolepec.gwt.shared.action.assegna.pec.RiassegnaPecIn;
import it.eng.portlet.consolepec.gwt.shared.action.assegna.pec.RiassegnaPecInResult;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class CambiaGruppoActionMockHandler implements ActionHandler<RiassegnaPecIn, RiassegnaPecInResult> {

	public CambiaGruppoActionMockHandler() {
	}

	@Override
	public RiassegnaPecInResult execute(RiassegnaPecIn action, ExecutionContext context) throws ActionException {
		RiassegnaPecInResult res = null;// new CambiaGruppoResult(dettaglio, "", false);
		return res;
	}

	@Override
	public void undo(RiassegnaPecIn action, RiassegnaPecInResult result, ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<RiassegnaPecIn> getActionType() {
		return RiassegnaPecIn.class;
	}
}
