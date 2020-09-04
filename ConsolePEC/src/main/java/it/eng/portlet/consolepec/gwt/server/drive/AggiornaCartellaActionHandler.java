package it.eng.portlet.consolepec.gwt.server.drive;

import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.exception.ConsoleDocumentaleException;
import it.eng.portlet.consolepec.gwt.server.rest.client.DriveClient;
import it.eng.portlet.consolepec.gwt.shared.drive.action.AggiornaCartellaAction;
import it.eng.portlet.consolepec.gwt.shared.drive.action.AggiornaCartellaResult;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Giacomo F.M.
 * @since 2019-06-12
 */
@Slf4j
public class AggiornaCartellaActionHandler implements ActionHandler<AggiornaCartellaAction, AggiornaCartellaResult> {

	@Autowired
	private DriveClient driveClient;

	@Override
	public AggiornaCartellaResult execute(AggiornaCartellaAction action, ExecutionContext context) throws ActionException {
		try {
			return new AggiornaCartellaResult(driveClient.aggiornaCartella(action.getCartella()));
		} catch (ConsoleDocumentaleException e) {
			log.error("Errore durante l'aggiornamento della cartella [" + action.getCartella() + "]", e);
			return new AggiornaCartellaResult(e.getOutputMessage());
		}
	}

	@Override
	public Class<AggiornaCartellaAction> getActionType() {
		return AggiornaCartellaAction.class;
	}

	@Override
	public void undo(AggiornaCartellaAction action, AggiornaCartellaResult result, ExecutionContext context) throws ActionException {
		// ~
	}

}
