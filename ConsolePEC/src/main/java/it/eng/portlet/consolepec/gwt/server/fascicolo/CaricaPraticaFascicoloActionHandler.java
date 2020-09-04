package it.eng.portlet.consolepec.gwt.server.fascicolo;

import java.util.Date;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.factory.XMLTaskFactory;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Stato;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.TaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.MettiInAffissioneTaskApi;
import it.eng.consolepec.xmlplugin.tasks.riattiva.DatiRiattivazioneTask;
import it.eng.consolepec.xmlplugin.tasks.riattiva.RiattivaFascicoloTask;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CaricaPraticaFascicoloAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CaricaPraticaFascicoloActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.GestioneTaskPratiche;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;

/**
 *
 * @author pluttero
 *
 */
public class CaricaPraticaFascicoloActionHandler implements ActionHandler<CaricaPraticaFascicoloAction, CaricaPraticaFascicoloActionResult> {

	@Autowired
	XMLPluginToDTOConverter utilPratica;

	@Autowired
	GestioneTaskPratiche gestioneTask;

	@Autowired
	PraticaSessionUtil praticaSessionUtil;

	@Autowired
	GestioneProfilazioneUtente gestioneProfilazioneUtente;

	Logger logger = LoggerFactory.getLogger(CaricaPraticaFascicoloActionHandler.class);

	public CaricaPraticaFascicoloActionHandler() {}

	@Override
	public CaricaPraticaFascicoloActionResult execute(CaricaPraticaFascicoloAction action, ExecutionContext context) throws ActionException {

		logger.debug("Caricamento fascicolo con encoded path: {}", action.getClientID());
		Fascicolo fascicolo;
		try {
			boolean isInSession = praticaSessionUtil.checkIfPraticaIsInSession(action.getClientID());
			fascicolo = praticaSessionUtil.loadFascicoloFromEncodedPath(action.getClientID(), action.getTipologiaCaricamento());

			if (!isInSession && gestioneProfilazioneUtente.getDatiUtente().isUtenteEsterno()) {
				fascicolo.accessoUtenteEsterno(gestioneProfilazioneUtente.getDatiUtente().getRuoloPersonale().getEtichetta());
				praticaSessionUtil.updateFascicolo(fascicolo);
			}

			logger.debug("Caricato fascicolo: {}", fascicolo.getDati());

			// metto il fascicolo a letto solamente se sono l'assegnatario del task
			Task<?> taskCorrente = gestioneTask.estraiTaskCorrente(fascicolo, gestioneProfilazioneUtente.getDatiUtente().getAnagraficheRuoli());

			/*
			 * pulizia bug produzione REDMINE n 205 se il task non è di tipo Riattiva e lo stato è in affissione va ripulita la pratica 1- si crea il task di riattivazione 2- lo stato va impostato a
			 * In Affissione 3- va disattivato il task fi Gestione
			 */
			if ((taskCorrente != null && !(taskCorrente instanceof RiattivaFascicoloTask)) && fascicolo.getDati().getStato().equals(Stato.IN_AFFISSIONE)
					&& fascicolo.getDati().getTipo().equals(TipologiaPratica.FASCICOLO_ALBO_PRETORIO)) {
				((MettiInAffissioneTaskApi) taskCorrente).mettiInAffissione();
				praticaSessionUtil.updatePratica(fascicolo);
				taskCorrente = gestioneTask.estraiTaskCorrente(fascicolo, gestioneProfilazioneUtente.getDatiUtente().getAnagraficheRuoli());
			}

			Task<?> taskCorrenteSoloAssegnatario = gestioneTask.estraiTaskCorrenteSoloAssegnatario(fascicolo);
			if (!fascicolo.getDati().isLetto() && taskCorrenteSoloAssegnatario != null) {
				fascicolo.getDati().setLetto(true);
				praticaSessionUtil.updatePratica(fascicolo);
			}

			/*
			 *
			 * pulizia bug del riporta in gestione Se un fascicolo contine mail collegate in caso di archiviazione non viene messo il task di riporta in gestione. Il codice seguente ripulisce i
			 * fascicoli coinvolti nel bug
			 */
			if (hasTaskGestione(fascicolo) == null && hasTaskRiattiva(fascicolo) == null) {
				RiattivaFascicoloTask task = null;
				Set<Task<?>> tasks = fascicolo.getTasks();
				for (Task<?> t : tasks) {

					// utente
					if (t instanceof RiattivaFascicoloTask && !t.isAttivo()) {
						task = (RiattivaFascicoloTask) t;
					}
				}

				if (task != null) {
					tasks.remove(task);
				}

				DatiRiattivazioneTask.Builder builderTask = new DatiRiattivazioneTask.Builder();
				builderTask.setAssegnatario(new Assegnatario(fascicolo.getTasks().iterator().next().getDati().getAssegnatario().getNome(),
						fascicolo.getTasks().iterator().next().getDati().getAssegnatario().getEtichetta(), new Date(System.currentTimeMillis() - 1000), null));
				builderTask.setAttivo(true);
				builderTask.setIdTaskDaRiattivare(fascicolo.getTasks().iterator().next().getDati().getId());
				DatiRiattivazioneTask riportaInGestioneTask = builderTask.construct();
				XMLTaskFactory xtf = new XMLTaskFactory();
				xtf.newTaskInstance(RiattivaFascicoloTask.class, fascicolo, riportaInGestioneTask);

				praticaSessionUtil.updatePratica(fascicolo);
				taskCorrente = gestioneTask.estraiTaskCorrente(fascicolo, gestioneProfilazioneUtente.getDatiUtente().getAnagraficheRuoli());
			}

			FascicoloDTO dto = utilPratica.fascicoloToDettaglio(fascicolo);

			return new CaricaPraticaFascicoloActionResult("", false, dto);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			return new CaricaPraticaFascicoloActionResult(e.getErrorMessage(), true, null);

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			return new CaricaPraticaFascicoloActionResult(ConsolePecConstants.ERROR_MESSAGE, true, null);

		}
	}

	private static Task<?> hasTaskGestione(Fascicolo fascicolo) {
		Task<?> task = null;
		for (Task<?> t : fascicolo.getTasks()) {
			if (t instanceof RiattivaFascicoloTask && t.isAttivo()) {
				task = t;
			}
		}
		return task;

	}

	private static Task<?> hasTaskRiattiva(Fascicolo fascicolo) {
		Task<?> task = null;
		for (Task<?> t : fascicolo.getTasks()) {
			if (t instanceof TaskFascicolo && t.isAttivo()) {
				task = t;
			}
		}
		return task;
	}

	@Override
	public void undo(CaricaPraticaFascicoloAction action, CaricaPraticaFascicoloActionResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<CaricaPraticaFascicoloAction> getActionType() {
		return CaricaPraticaFascicoloAction.class;
	}

}
