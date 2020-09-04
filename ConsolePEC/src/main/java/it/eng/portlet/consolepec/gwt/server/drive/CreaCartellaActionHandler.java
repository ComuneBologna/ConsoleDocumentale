package it.eng.portlet.consolepec.gwt.server.drive;

import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.drive.Cartella;
import it.eng.cobo.consolepec.commons.exception.ConsoleDocumentaleException;
import it.eng.portlet.consolepec.gwt.server.rest.client.DriveClient;
import it.eng.portlet.consolepec.gwt.shared.drive.action.CreaCartellaAction;
import it.eng.portlet.consolepec.gwt.shared.drive.action.CreaCartellaResult;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Giacomo F.M.
 * @since 2019-06-25
 */
@Slf4j
public class CreaCartellaActionHandler implements ActionHandler<CreaCartellaAction, CreaCartellaResult> {

	@Autowired
	private DriveClient driveClient;

	@Override
	public CreaCartellaResult execute(CreaCartellaAction action, ExecutionContext context) throws ActionException {
		try {
			Cartella c = driveClient.creaCartella(action.getCartella(), action.getRuolo());
			log.debug("Creazione cartella con id ({})", c.getId());
			return new CreaCartellaResult(c);
		} catch (ConsoleDocumentaleException e) {
			log.error("Errore nella creazione della cartella", e);
			return new CreaCartellaResult(e.getOutputMessage());
		}
	}

	@Override
	public Class<CreaCartellaAction> getActionType() {
		return CreaCartellaAction.class;
	}

	@Override
	public void undo(CreaCartellaAction action, CreaCartellaResult result, ExecutionContext context) throws ActionException {/**/}

}
