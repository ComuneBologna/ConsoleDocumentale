package it.eng.portlet.consolepec.gwt.server.profilazione;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.profilazione.CaricaDatiUtenteAction;
import it.eng.portlet.consolepec.gwt.shared.action.profilazione.CaricaDatiUtenteResult;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

/**
 *
 * @author biagiot
 *
 */
public class CaricaDatiUtenteActionHandler implements ActionHandler<CaricaDatiUtenteAction, CaricaDatiUtenteResult> {

	private static final Logger logger = LoggerFactory.getLogger(CaricaDatiUtenteActionHandler.class);

	@Autowired
	GestioneProfilazioneUtente gestioneProfilazioneUtente;

	@Autowired
	UserSessionUtil userSessionUtil;

	@Override
	public CaricaDatiUtenteResult execute(CaricaDatiUtenteAction action, ExecutionContext ec) throws ActionException {
		logger.info("Inizio CaricaDatiUtenteActionHandler");

		CaricaDatiUtenteResult result = null;

		try {
			result = new CaricaDatiUtenteResult(gestioneProfilazioneUtente.getDatiUtente());

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			result = new CaricaDatiUtenteResult(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			result = new CaricaDatiUtenteResult(ConsolePecConstants.ERROR_MESSAGE);

		}

		logger.info("Fine CaricaDatiUtenteActionHandler");
		return result;
	}

	@Override
	public Class<CaricaDatiUtenteAction> getActionType() {
		return CaricaDatiUtenteAction.class;
	}

	@Override
	public void undo(CaricaDatiUtenteAction arg0, CaricaDatiUtenteResult arg1, ExecutionContext arg2) throws ActionException {}
}
