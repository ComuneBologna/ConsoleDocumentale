package it.eng.portlet.consolepec.gwt.server.drive;

import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.drive.DriveElement;
import it.eng.cobo.consolepec.commons.exception.ConsoleDocumentaleException;
import it.eng.portlet.consolepec.gwt.server.rest.client.DriveClient;
import it.eng.portlet.consolepec.gwt.shared.drive.action.CercaElementoAction;
import it.eng.portlet.consolepec.gwt.shared.drive.action.CercaElementoResult;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Giacomo F.M.
 * @since 2019-05-31
 */
@Slf4j
public class CercaElementoActionHandler implements ActionHandler<CercaElementoAction, CercaElementoResult> {

	@Autowired
	private DriveClient driveClient;

	@Override
	public CercaElementoResult execute(CercaElementoAction action, ExecutionContext context) throws ActionException {
		try {
			DriveElement elemento = driveClient.cercaElemento(action.getId());
			return new CercaElementoResult(elemento);
		} catch (ConsoleDocumentaleException e) {
			log.error("Errore nella ricerca della cartella", e);
			return new CercaElementoResult(e.getOutputMessage());
		}
	}

	@Override
	public Class<CercaElementoAction> getActionType() {
		return CercaElementoAction.class;
	}

	@Override
	public void undo(CercaElementoAction action, CercaElementoResult result, ExecutionContext context) throws ActionException {/**/}

}
