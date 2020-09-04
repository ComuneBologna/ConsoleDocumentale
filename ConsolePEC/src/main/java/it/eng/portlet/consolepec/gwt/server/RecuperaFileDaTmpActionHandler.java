package it.eng.portlet.consolepec.gwt.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.RecuperaFileDaTmpAction;
import it.eng.portlet.consolepec.gwt.shared.action.RecuperaFileDaTmpResult;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.impl.GestioneAllegati;

/**
 *
 * @author biagiot
 *
 */
public class RecuperaFileDaTmpActionHandler implements ActionHandler<RecuperaFileDaTmpAction, RecuperaFileDaTmpResult> {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	GestioneAllegati gestioneAllegati;

	public RecuperaFileDaTmpActionHandler() {

	}

	@Override
	public RecuperaFileDaTmpResult execute(RecuperaFileDaTmpAction action, ExecutionContext context) throws ActionException {

		logger.info("Start RecuperaFileDaFileSystemActionHandler");
		RecuperaFileDaTmpResult result = new RecuperaFileDaTmpResult(ConsolePecConstants.ERROR_MESSAGE);

		try {
			if (action.getTempFiles() != null) {
				result = new RecuperaFileDaTmpResult(gestioneAllegati.getFilesFromFileTmp(action.getTempFiles()));
			}

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			result.setError(true);
			result.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
		}

		logger.info("End RecuperaFileDaFileSystemActionHandler");
		return result;

	}

	@Override
	public Class<RecuperaFileDaTmpAction> getActionType() {
		return RecuperaFileDaTmpAction.class;
	}

	@Override
	public void undo(RecuperaFileDaTmpAction action, RecuperaFileDaTmpResult result, ExecutionContext context) throws ActionException {}

}
