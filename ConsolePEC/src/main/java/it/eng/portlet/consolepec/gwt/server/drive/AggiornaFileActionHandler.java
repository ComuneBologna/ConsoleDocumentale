package it.eng.portlet.consolepec.gwt.server.drive;

import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.exception.ConsoleDocumentaleException;
import it.eng.portlet.consolepec.gwt.server.rest.client.DriveClient;
import it.eng.portlet.consolepec.gwt.shared.drive.action.AggiornaFileAction;
import it.eng.portlet.consolepec.gwt.shared.drive.action.AggiornaFileResult;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Giacomo F.M.
 * @since 2019-06-12
 */
@Slf4j
public class AggiornaFileActionHandler implements ActionHandler<AggiornaFileAction, AggiornaFileResult> {

	@Autowired
	private DriveClient driveClient;

	@Override
	public AggiornaFileResult execute(AggiornaFileAction action, ExecutionContext context) throws ActionException {
		try {
			return new AggiornaFileResult(driveClient.aggiornaFile(action.getFile()));
		} catch (ConsoleDocumentaleException e) {
			log.error("Errore durante l'aggiornamento del file [" + action.getFile() + "]", e);
			return new AggiornaFileResult(e.getOutputMessage());
		}
	}

	@Override
	public Class<AggiornaFileAction> getActionType() {
		return AggiornaFileAction.class;
	}

	@Override
	public void undo(AggiornaFileAction action, AggiornaFileResult result, ExecutionContext context) throws ActionException {
		// ~
	}

}
