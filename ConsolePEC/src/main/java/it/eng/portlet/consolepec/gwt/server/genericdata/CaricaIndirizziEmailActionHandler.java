package it.eng.portlet.consolepec.gwt.server.genericdata;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.portlet.consolepec.gwt.shared.action.genericdata.CaricaIndirizziEmailAction;
import it.eng.portlet.consolepec.gwt.shared.action.genericdata.CaricaIndirizziEmailActionResult;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;
import it.eng.portlet.consolepec.spring.genericdata.IndirizzoEmailCachePrimoLivello;
import it.eng.portlet.consolepec.spring.genericdata.IndirizzoEmailCacheSecondoLivello;

/**
 * 
 * @author biagiot
 *
 */
public class CaricaIndirizziEmailActionHandler implements ActionHandler<CaricaIndirizziEmailAction, CaricaIndirizziEmailActionResult> {

	private Logger logger = LoggerFactory.getLogger(CaricaIndirizziEmailActionHandler.class);

	@Autowired
	IndirizzoEmailCacheSecondoLivello indirizzoEmailCacheSecondoLivello;
	@Autowired
	IndirizzoEmailCachePrimoLivello indirizzoEmailCachePrimoLivello;
	@Autowired
	GestioneProfilazioneUtente gestioneProfilazioneUtente;
	@Autowired
	UserSessionUtil userSessionUtil;

	@Override
	public CaricaIndirizziEmailActionResult execute(CaricaIndirizziEmailAction action, ExecutionContext context) throws ActionException {
		logger.info("Start CaricaIndirizziEmailActionHandler");

		CaricaIndirizziEmailActionResult caricaIndirizziEmailActionResult = null;

		if (!action.isCaricaCache()) {
			logger.info("Inizio recupero indirizzi email");

			try {
				List<String> indirizziEmail = indirizzoEmailCacheSecondoLivello.getIndirizziEmail(action.getChiave(), gestioneProfilazioneUtente.getDatiUtente());

				if (!indirizziEmail.isEmpty()) {
					caricaIndirizziEmailActionResult = new CaricaIndirizziEmailActionResult(indirizziEmail);
				} else {
					caricaIndirizziEmailActionResult = new CaricaIndirizziEmailActionResult("Nessun indirizzo email trovato");
				}

			} catch (Exception e) {
				logger.error("Errore nel recupero degli indirizzi email", e);
				caricaIndirizziEmailActionResult = new CaricaIndirizziEmailActionResult(e.getMessage());
			}

			logger.info("Fine recupero indirizzi email");

		} else {
			logger.info("Inizio caricamento cache");

			try {
				indirizzoEmailCachePrimoLivello.getAllIndirizziEmail(userSessionUtil.getUtenteSpagic());
				caricaIndirizziEmailActionResult = new CaricaIndirizziEmailActionResult();

			} catch (Exception e) {
				logger.error("Errore nel caricamento della cache", e);
				caricaIndirizziEmailActionResult = new CaricaIndirizziEmailActionResult(e.getMessage());
			}

			logger.info("Fine caricamento cache");
		}

		logger.info("End CaricaIndirizziEmailActionHandler");
		return caricaIndirizziEmailActionResult;

	}

	@Override
	public Class<CaricaIndirizziEmailAction> getActionType() {
		return CaricaIndirizziEmailAction.class;
	}

	@Override
	public void undo(CaricaIndirizziEmailAction action, CaricaIndirizziEmailActionResult result, ExecutionContext context) throws ActionException {

	}

}
