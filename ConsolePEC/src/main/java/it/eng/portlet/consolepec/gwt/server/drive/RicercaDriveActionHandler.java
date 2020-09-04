package it.eng.portlet.consolepec.gwt.server.drive;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.drive.DriveElement;
import it.eng.cobo.consolepec.commons.exception.ConsoleDocumentaleException;
import it.eng.portlet.consolepec.gwt.server.rest.client.DriveClient;
import it.eng.portlet.consolepec.gwt.shared.drive.action.RicercaDriveAction;
import it.eng.portlet.consolepec.gwt.shared.drive.action.RicercaDriveResult;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Giacomo F.M.
 * @since 2019-07-12
 */
@Slf4j
public class RicercaDriveActionHandler implements ActionHandler<RicercaDriveAction, RicercaDriveResult> {

	@Autowired
	private DriveClient driveClient;

	@Override
	public RicercaDriveResult execute(final RicercaDriveAction action, final ExecutionContext context) throws ActionException {
		try {
			List<DriveElement> elements = driveClient.ricerca(action.getQuery());
			return new RicercaDriveResult(elements);
		} catch (ConsoleDocumentaleException e) {
			log.error("Errore nella ricerca", e);
			return new RicercaDriveResult(e.getOutputMessage());
		}
	}

	@Override
	public Class<RicercaDriveAction> getActionType() {
		return RicercaDriveAction.class;
	}

	@Override
	public void undo(RicercaDriveAction action, RicercaDriveResult result, ExecutionContext context) throws ActionException {
		// ~
	}

}
