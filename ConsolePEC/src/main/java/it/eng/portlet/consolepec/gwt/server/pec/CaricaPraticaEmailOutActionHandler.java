package it.eng.portlet.consolepec.gwt.server.pec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailOut;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Stato;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.TaskFascicolo;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CaricaPraticaEmailOutAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CaricaPraticaEmailOutActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.GestioneTaskPratiche;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;

/**
 * Carica una pratica di tipo PraticamailOut, ritornando un dto PecOutDTO
 *
 * @author pluttero
 *
 */
public class CaricaPraticaEmailOutActionHandler implements ActionHandler<CaricaPraticaEmailOutAction, CaricaPraticaEmailOutActionResult> {
	private Logger logger = LoggerFactory.getLogger(CaricaPraticaEmailOutActionHandler.class);

	@Autowired
	XMLPluginToDTOConverter utilPratica;

	@Autowired
	PraticaSessionUtil praticaSessionUtil;

	@Autowired
	GestioneTaskPratiche gestioneTask;

	@Autowired
	GestioneProfilazioneUtente gestioneProfilazioneUtente;

	public CaricaPraticaEmailOutActionHandler() {}

	@Override
	public CaricaPraticaEmailOutActionResult execute(CaricaPraticaEmailOutAction action, ExecutionContext context) throws ActionException {
		logger.debug("Richiesta PraticaEmailOut con clientId: {}", action.getClientId());
		CaricaPraticaEmailOutActionResult res = null;
		try {
			PraticaEmailOut email = praticaSessionUtil.loadPraticaEmailOutFromEncodedPath(action.getClientId(), action.getTipologiaCaricamento());
			PecOutDTO dto = utilPratica.emailToDettaglioOUT(email);

			boolean reinoltrabile = false;
			for (String idPraticaCollegata : dto.getIdPraticheCollegate()) {
				Pratica<?> pratica = praticaSessionUtil.loadPraticaFromEncodedPath(idPraticaCollegata, TipologiaCaricamento.CARICA);
				if (pratica instanceof Fascicolo) {
					Fascicolo fascicolo = (Fascicolo) pratica;
					if (!fascicolo.getDati().getStato().equals(Stato.ARCHIVIATO)) {
						reinoltrabile = true;
					}
					Task<?> taskCorrente = gestioneTask.estraiTaskCorrente(fascicolo, TaskFascicolo.class, gestioneProfilazioneUtente.getDatiUtente().getAnagraficheRuoli());
					if (taskCorrente == null) {
						reinoltrabile = false;
						break;
					}
				}
			}
			dto.setReinoltrabile(reinoltrabile);
			res = new CaricaPraticaEmailOutActionResult(dto);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			res = new CaricaPraticaEmailOutActionResult(null);
			res.setError(true);
			res.setErrorMessage(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			res = new CaricaPraticaEmailOutActionResult(null);
			res.setError(true);
			res.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);

		} finally {
			logger.debug("Fine richiesta caricamento");
		}
		return res;
	}

	@Override
	public void undo(CaricaPraticaEmailOutAction action, CaricaPraticaEmailOutActionResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<CaricaPraticaEmailOutAction> getActionType() {
		return CaricaPraticaEmailOutAction.class;
	}
}
