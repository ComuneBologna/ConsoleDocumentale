package it.eng.portlet.consolepec.gwt.server.drive;

import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.exception.ConsoleDocumentaleException;
import it.eng.portlet.consolepec.gwt.server.rest.client.DriveClient;
import it.eng.portlet.consolepec.gwt.shared.drive.action.EliminaElementoAction;
import it.eng.portlet.consolepec.gwt.shared.drive.action.EliminaElementoResult;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Giacomo F.M.
 * @since 2019-07-09
 */
@Slf4j
public class EliminaElementoActionHandler implements ActionHandler<EliminaElementoAction, EliminaElementoResult> {

	@Autowired
	private DriveClient driveClient;

	@Override
	public EliminaElementoResult execute(EliminaElementoAction action, ExecutionContext context) throws ActionException {
		try {
			log.debug("Eliminazione dell'elemento ({})", action.getElemento().getId());
			driveClient.eliminaElemento(action.getElemento());
			return new EliminaElementoResult();
		} catch (ConsoleDocumentaleException e) {
			log.error("Errore nella cancellazione dell'elemento", e);
			return new EliminaElementoResult(e.getOutputMessage());
		}
	}

	@Override
	public Class<EliminaElementoAction> getActionType() {
		return EliminaElementoAction.class;
	}

	@Override
	public void undo(EliminaElementoAction action, EliminaElementoResult result, ExecutionContext context) throws ActionException {
		// ~
	}

}
