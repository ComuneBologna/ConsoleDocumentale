package it.eng.portlet.consolepec.gwt.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.CaricaPraticaAction;
import it.eng.portlet.consolepec.gwt.shared.action.CaricaPraticaActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;

public class CaricaPraticaActionActionHandler implements ActionHandler<CaricaPraticaAction, CaricaPraticaActionResult> {

	private static final Logger logger = LoggerFactory.getLogger(CaricaPraticaActionActionHandler.class);

	@Autowired
	PraticaSessionUtil praticaSessionUtil;

	@Autowired
	XMLPluginToDTOConverter utilPratica;

	public CaricaPraticaActionActionHandler() {}

	@Override
	public CaricaPraticaActionResult execute(CaricaPraticaAction action, ExecutionContext context) throws ActionException {

		CaricaPraticaActionResult result = new CaricaPraticaActionResult();

		try {

			if (action.getTipologiaCaricamento().equals(TipologiaCaricamento.RICARICA)) {
				praticaSessionUtil.removePraticaFromEncodedPath(action.getClientID());
			}
			Pratica<?> loadPraticaFromEncodedPath = praticaSessionUtil.loadPraticaFromEncodedPath(action.getClientID());
			PraticaDTO praticaToDTO = utilPratica.praticaToDTO(loadPraticaFromEncodedPath);
			result.setPraticaDTO(praticaToDTO);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			result.setError(true);
			result.setErrorMsg(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			result.setError(true);
			result.setErrorMsg(ConsolePecConstants.ERROR_MESSAGE);
		}

		return result;
	}

	@Override
	public void undo(CaricaPraticaAction action, CaricaPraticaActionResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<CaricaPraticaAction> getActionType() {
		return CaricaPraticaAction.class;
	}
}
