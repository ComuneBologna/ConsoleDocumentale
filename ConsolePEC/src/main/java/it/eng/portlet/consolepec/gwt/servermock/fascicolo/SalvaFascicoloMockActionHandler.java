package it.eng.portlet.consolepec.gwt.servermock.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.SalvaFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.SalvaFascicoloResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class SalvaFascicoloMockActionHandler implements ActionHandler<SalvaFascicolo, SalvaFascicoloResult> {

	public SalvaFascicoloMockActionHandler() {
	}

	@Override
	public SalvaFascicoloResult execute(SalvaFascicolo action, ExecutionContext context) throws ActionException {
		//simulazione di salvataggio, ritorniamo l'input sull'output
		FascicoloDTO input = action.getFascicolo();
		FascicoloDB.getInstance().setDettaglio(input);
		SalvaFascicoloResult result = new SalvaFascicoloResult(input, false, null);
		return result;
	}

	@Override
	public void undo(SalvaFascicolo action, SalvaFascicoloResult result, ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<SalvaFascicolo> getActionType() {
		return SalvaFascicolo.class;
	}
}
