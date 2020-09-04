package it.eng.portlet.consolepec.gwt.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gwt.thirdparty.guava.common.base.Strings;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.EliminaFileDaTmpAction;
import it.eng.portlet.consolepec.gwt.shared.action.EliminaFileDaTmpResult;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.impl.GestioneAllegati;

public class EliminaFileDaTmpActionHandler implements ActionHandler<EliminaFileDaTmpAction, EliminaFileDaTmpResult> {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	GestioneAllegati gestioneAllegati;

	public EliminaFileDaTmpActionHandler() {

	}

	@Override
	public EliminaFileDaTmpResult execute(EliminaFileDaTmpAction action, ExecutionContext context) throws ActionException {
		logger.info("Start EliminaFileDaTmpActionHandler");

		EliminaFileDaTmpResult result = new EliminaFileDaTmpResult(ConsolePecConstants.ERROR_MESSAGE);

		try {

			if (!Strings.isNullOrEmpty(action.getPathFile())) {
				result = new EliminaFileDaTmpResult(gestioneAllegati.eliminaFileTmp(action.getPathFile()));
			}

		} catch (Exception e) {
			result = new EliminaFileDaTmpResult(ConsolePecConstants.ERROR_MESSAGE);
			logger.error(e.getLocalizedMessage(), e);
		}

		logger.info("End EliminaFileDaTmpActionHandler");
		return result;
	}

	@Override
	public Class<EliminaFileDaTmpAction> getActionType() {
		return EliminaFileDaTmpAction.class;
	}

	@Override
	public void undo(EliminaFileDaTmpAction action, EliminaFileDaTmpResult result, ExecutionContext context) throws ActionException {}
}
