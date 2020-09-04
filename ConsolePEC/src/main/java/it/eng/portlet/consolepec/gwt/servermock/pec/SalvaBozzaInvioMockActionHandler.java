package it.eng.portlet.consolepec.gwt.servermock.pec;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.portlet.consolepec.gwt.shared.action.pec.SalvaBozzaInvio;
import it.eng.portlet.consolepec.gwt.shared.action.pec.SalvaBozzaInvioResult;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;

public class SalvaBozzaInvioMockActionHandler implements ActionHandler<SalvaBozzaInvio, SalvaBozzaInvioResult> {

	public SalvaBozzaInvioMockActionHandler() {

	}

	@Override
	public SalvaBozzaInvioResult execute(SalvaBozzaInvio action, ExecutionContext context) throws ActionException {
		PecOutDTO bozzaPecOut = action.getBozzaPecOut();

		AllegatoDTO a = new AllegatoDTO("fileMock.pdf", null, null, bozzaPecOut.getClientID(), "0.1");
		bozzaPecOut.getAllegati().add(a);
		SalvaBozzaInvioResult savlaBozza = new SalvaBozzaInvioResult(false, "", bozzaPecOut);
		return savlaBozza;
	}

	@Override
	public void undo(SalvaBozzaInvio action, SalvaBozzaInvioResult result, ExecutionContext context) throws ActionException {
		//
	}

	@Override
	public Class<SalvaBozzaInvio> getActionType() {

		return SalvaBozzaInvio.class;
	}
}
