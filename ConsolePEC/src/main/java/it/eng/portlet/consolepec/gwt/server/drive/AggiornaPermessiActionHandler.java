package it.eng.portlet.consolepec.gwt.server.drive;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.drive.permessi.PermessoDrive;
import it.eng.cobo.consolepec.commons.exception.ConsoleDocumentaleException;
import it.eng.portlet.consolepec.gwt.server.rest.client.DriveClient;
import it.eng.portlet.consolepec.gwt.shared.drive.action.AggiornaPermessiAction;
import it.eng.portlet.consolepec.gwt.shared.drive.action.AggiornaPermessiResult;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Giacomo F.M.
 * @since 2019-07-05
 */
@Slf4j
public class AggiornaPermessiActionHandler implements ActionHandler<AggiornaPermessiAction, AggiornaPermessiResult> {

	@Autowired
	private DriveClient driveClient;

	@Override
	public AggiornaPermessiResult execute(AggiornaPermessiAction action, ExecutionContext context) throws ActionException {
		try {
			List<PermessoDrive> aggiunti = new ArrayList<>(action.getAggiunta());
			List<PermessoDrive> rimossi = new ArrayList<>(action.getRimozione());
			return new AggiornaPermessiResult(driveClient.aggiornaPermessi(action.getId(), action.isRecursive(), aggiunti, rimossi));
		} catch (ConsoleDocumentaleException e) {
			log.error("Errore durante l'aggiornamento dei permessi", e);
			return new AggiornaPermessiResult(e.getOutputMessage());
		}
	}

	@Override
	public Class<AggiornaPermessiAction> getActionType() {
		return AggiornaPermessiAction.class;
	}

	@Override
	public void undo(AggiornaPermessiAction action, AggiornaPermessiResult result, ExecutionContext context) throws ActionException {
		// ~
	}

}
