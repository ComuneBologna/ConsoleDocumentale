package it.eng.portlet.consolepec.gwt.server.profilazione;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.profilazione.CaricaWorklistAction;
import it.eng.portlet.consolepec.gwt.shared.action.profilazione.CaricaWorklistResult;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;

public class CaricaWorklistActionHandler implements ActionHandler<CaricaWorklistAction, CaricaWorklistResult> {

	@Autowired
	GestioneProfilazioneUtente gestioneProfilazioneUtente;

	private static final Logger logger = LoggerFactory.getLogger(CaricaWorklistActionHandler.class);

	@Override
	public CaricaWorklistResult execute(CaricaWorklistAction action, ExecutionContext arg1) throws ActionException {

		logger.info("Inizio caricamento worklist");
		CaricaWorklistResult result = null;

		try {
			result = new CaricaWorklistResult(gestioneProfilazioneUtente.getWorklistAbilitate(action.isRicaricaContatori()));

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			result = new CaricaWorklistResult(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			result = new CaricaWorklistResult(ConsolePecConstants.ERROR_MESSAGE);

		}

		logger.info("Fine caricamento worklist");
		return result;
	}

	@Override
	public Class<CaricaWorklistAction> getActionType() {
		return CaricaWorklistAction.class;
	}

	@Override
	public void undo(CaricaWorklistAction arg0, CaricaWorklistResult arg1, ExecutionContext arg2) throws ActionException {}

}
