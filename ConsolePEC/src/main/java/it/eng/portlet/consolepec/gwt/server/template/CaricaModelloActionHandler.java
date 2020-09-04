package it.eng.portlet.consolepec.gwt.server.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.template.CaricaModelloAction;
import it.eng.portlet.consolepec.gwt.shared.action.template.CaricaModelloResult;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.GestioneTaskPratiche;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class CaricaModelloActionHandler implements ActionHandler<CaricaModelloAction, CaricaModelloResult> {

	@Autowired
	XMLPluginToDTOConverter utilPratica;
	@Autowired
	UserSessionUtil userSessionUtil;
	@Autowired
	GestioneTaskPratiche gestioneTask;
	@Autowired
	PraticaSessionUtil praticaSessionUtil;

	private static final Logger logger = LoggerFactory.getLogger(CaricaModelloActionHandler.class);

	@Override
	public CaricaModelloResult execute(CaricaModelloAction action, ExecutionContext arg1) throws ActionException {

		logger.info("Start CaricaModelloActionHandler");
		CaricaModelloResult result = new CaricaModelloResult();

		try {
			result.setModello(utilPratica.modelloToDettaglio(praticaSessionUtil.loadModelloFromEncodedPath(action.getClientID(), action.getTipologiaCaricamento())));

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			result = new CaricaModelloResult(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			result = new CaricaModelloResult(ConsolePecConstants.ERROR_MESSAGE);

		}

		logger.info("Start CaricaModelloActionHandler");
		return result;
	}

	@Override
	public Class<CaricaModelloAction> getActionType() {
		return CaricaModelloAction.class;
	}

	@Override
	public void undo(CaricaModelloAction arg0, CaricaModelloResult arg1, ExecutionContext arg2) throws ActionException {}
}
