package it.eng.portlet.consolepec.gwt.servermock.pec;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.portlet.consolepec.gwt.servermock.PecOutDB;
import it.eng.portlet.consolepec.gwt.shared.action.pec.UploadAllegatoPraticaAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.UploadAllegatoPraticaResult;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;

public class UploadAllegatoPraticaEmailOutMockActionHandler implements ActionHandler<UploadAllegatoPraticaAction, UploadAllegatoPraticaResult> {

	public UploadAllegatoPraticaEmailOutMockActionHandler() {

	}

	@Override
	public UploadAllegatoPraticaResult execute(UploadAllegatoPraticaAction action, ExecutionContext context) throws ActionException {
		PecOutDTO dto = PecOutDB.getInstance().getDettaglio(action.getClientID());

		AllegatoDTO allegato = new AllegatoDTO("nuovoallegato" + dto.getAllegati().size() + ".txt", null, null, dto.getClientID(), "0.1");

		dto.getAllegati().add(allegato);

		return new UploadAllegatoPraticaResult("", false, dto);
	}

	@Override
	public void undo(UploadAllegatoPraticaAction action, UploadAllegatoPraticaResult result, ExecutionContext context) throws ActionException {

	}

	@Override
	public Class<UploadAllegatoPraticaAction> getActionType() {

		return UploadAllegatoPraticaAction.class;
	}
}
