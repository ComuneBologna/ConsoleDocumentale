package it.eng.portlet.consolepec.gwt.server.fascicolo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.TaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.SalvaFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.SalvaFascicoloResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.GestioneTaskPratiche;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class SalvaFascicoloActionHandler implements ActionHandler<SalvaFascicolo, SalvaFascicoloResult> {

	@Autowired
	private XMLPluginToDTOConverter utilPratica;
	@Autowired
	PraticaSessionUtil praticaSessionUtil;
	@Autowired
	GestioneTaskPratiche gestioneTask;
	@Autowired
	UserSessionUtil userSessionUtil;
	@Autowired
	GestioneProfilazioneUtente gestioneProfilazioneUtente;

	public SalvaFascicoloActionHandler() {}

	@Override
	public SalvaFascicoloResult execute(SalvaFascicolo action, ExecutionContext context) throws ActionException {
		Logger logger = LoggerFactory.getLogger(SalvaFascicoloActionHandler.class);
		FascicoloDTO input = action.getFascicolo();
		SalvaFascicoloResult result = null;
		try {
			logger.debug("Carico fascicolo con path: {}", input.getClientID());
			Fascicolo fascicolo = praticaSessionUtil.loadFascicoloFromEncodedPath(input.getClientID(), TipologiaCaricamento.RICARICA);

			logger.debug("Caricato fascicolo: {}", fascicolo.getDati());
			logger.debug("Aggiornato fascicolo su spagic");
			dtoToFascicolo(action.getFascicolo(), fascicolo);
			praticaSessionUtil.updateFascicolo(fascicolo);
			FascicoloDTO dtoOut = utilPratica.fascicoloToDettaglio(fascicolo);
			result = new SalvaFascicoloResult(dtoOut, false, null);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			result = new SalvaFascicoloResult(null, true, e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			result = new SalvaFascicoloResult(null, true, ConsolePecConstants.ERROR_MESSAGE);

		} finally {
			logger.debug("Fine salva fascicolo");
		}

		return result;
	}

	protected Fascicolo dtoToFascicolo(FascicoloDTO in, Fascicolo out) {

		DatiFascicolo dati = out.getDati();

		// note e dati aggiuntivi sono modificabili
		dati.setNote(in.getNote());

		TaskFascicolo<?> taskFascicolo = gestioneTask.estraiTaskCorrente(out, TaskFascicolo.class, gestioneProfilazioneUtente.getDatiUtente().getAnagraficheRuoli());

		if (taskFascicolo == null || !taskFascicolo.controllaAbilitazione(TipoApiTask.MODIFICA_DATO_AGGIUNTIVO)) {
			throw new IllegalArgumentException("Utente non abilitato al task di modifica dati aggiuntivi per il fascicolo " + out.getDati().getIdDocumentale());
		}

		if (in.getValoriDatiAggiuntivi().isEmpty() == false) {
			for (DatoAggiuntivo v : in.getValoriDatiAggiuntivi()) {
				taskFascicolo.modificaDatoAggiuntivo(v);
			}
		}

		return out;
	}

	@Override
	public void undo(SalvaFascicolo action, SalvaFascicoloResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<SalvaFascicolo> getActionType() {
		return SalvaFascicolo.class;
	}
}
