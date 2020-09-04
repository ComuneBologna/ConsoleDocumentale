package it.eng.portlet.consolepec.gwt.server.drive;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.drive.Cartella;
import it.eng.cobo.consolepec.commons.drive.DriveElement;
import it.eng.cobo.consolepec.commons.exception.ConsoleDocumentaleException;
import it.eng.portlet.consolepec.gwt.server.rest.client.DriveClient;
import it.eng.portlet.consolepec.gwt.shared.drive.action.ApriCartellaAction;
import it.eng.portlet.consolepec.gwt.shared.drive.action.ApriCartellaResult;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Giacomo F.M.
 * @since 2019-05-31
 */
@Slf4j
public class ApriCartellaActionHandler implements ActionHandler<ApriCartellaAction, ApriCartellaResult> {

	@Autowired
	private DriveClient driveClient;

	@Override
	public ApriCartellaResult execute(ApriCartellaAction action, ExecutionContext context) throws ActionException {
		try {
			Cartella cartella = driveClient.cercaCartella(action.getIdCartella());
			List<DriveElement> element = driveClient.apriCartella(action.getIdCartella(), action.getPage(), action.getLimit());
			return new ApriCartellaResult(cartella, element);
		} catch (ConsoleDocumentaleException e) {
			log.error("Errore nella ricerca della cartella", e);
			return new ApriCartellaResult(e.getOutputMessage());
		}
	}

	@Override
	public Class<ApriCartellaAction> getActionType() {
		return ApriCartellaAction.class;
	}

	@Override
	public void undo(ApriCartellaAction action, ApriCartellaResult result, ExecutionContext context) throws ActionException {/**/}

}
