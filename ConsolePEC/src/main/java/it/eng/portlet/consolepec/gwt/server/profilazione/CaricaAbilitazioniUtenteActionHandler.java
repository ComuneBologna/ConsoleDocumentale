package it.eng.portlet.consolepec.gwt.server.profilazione;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.profilazione.CaricaAbilitazioniUtenteAction;
import it.eng.portlet.consolepec.gwt.shared.action.profilazione.CaricaAbilitazioniUtenteResult;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;

/**
 *
 * @author biagiot
 *
 */
public class CaricaAbilitazioniUtenteActionHandler implements ActionHandler<CaricaAbilitazioniUtenteAction, CaricaAbilitazioniUtenteResult> {

	private static final Logger logger = LoggerFactory.getLogger(CaricaAbilitazioniUtenteActionHandler.class);

	@Autowired
	GestioneProfilazioneUtente gestioneProfilazioneUtente;

	@Override
	public CaricaAbilitazioniUtenteResult execute(CaricaAbilitazioniUtenteAction arg0, ExecutionContext arg1) throws ActionException {
		logger.info("Inizio CaricaAbilitazioniUtenteActionHandler");
		CaricaAbilitazioniUtenteResult result = null;

		try {
			result = new CaricaAbilitazioniUtenteResult(gestioneProfilazioneUtente.getAutorizzazioniUtente());

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			result = new CaricaAbilitazioniUtenteResult(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			result = new CaricaAbilitazioniUtenteResult(ConsolePecConstants.ERROR_MESSAGE);
		}

		logger.info("Fine CaricaAbilitazioniUtenteActionHandler");
		return result;
	}

	@Override
	public Class<CaricaAbilitazioniUtenteAction> getActionType() {
		return CaricaAbilitazioniUtenteAction.class;
	}

	@Override
	public void undo(CaricaAbilitazioniUtenteAction arg0, CaricaAbilitazioniUtenteResult arg1, ExecutionContext arg2) throws ActionException {}
}
