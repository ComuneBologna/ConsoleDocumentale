package it.eng.portlet.consolepec.gwt.servermock.pec;

import it.eng.portlet.consolepec.gwt.servermock.PecOutDB;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CancellaAllegatoPecOut;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CancellaAllegatoPecOutResult;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class CancellaAllegatoPecOutMockActionHandler
	implements
	ActionHandler<CancellaAllegatoPecOut, CancellaAllegatoPecOutResult> {



	public CancellaAllegatoPecOutMockActionHandler() {

	}

	@Override
	public CancellaAllegatoPecOutResult execute(CancellaAllegatoPecOut action, ExecutionContext context) throws ActionException {

		PecOutDTO pecOut = PecOutDB.getInstance().getDettaglio(action.getClientID());
		/*
		AllegatoDTO allegatoDaRimuovere = null;
		for( AllegatoDTO allegato :pecOut.getAllegati()){
			if(allegato.getNome().equals(action.getNomeAllegato())){
				allegatoDaRimuovere = allegato;
			}
		}
		pecOut.getAllegati().remove( allegatoDaRimuovere );*/

		return new CancellaAllegatoPecOutResult("", false, pecOut);
	}

	@Override
	public void undo(
		CancellaAllegatoPecOut action, CancellaAllegatoPecOutResult result,
		ExecutionContext context)
		throws ActionException {

	}

	@Override
	public Class<CancellaAllegatoPecOut> getActionType() {

		return CancellaAllegatoPecOut.class;
	}
}
