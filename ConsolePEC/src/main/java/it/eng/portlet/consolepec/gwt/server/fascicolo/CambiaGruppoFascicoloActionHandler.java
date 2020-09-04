package it.eng.portlet.consolepec.gwt.server.fascicolo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente.PreferenzeRiassegnazione;
import it.eng.consolepec.client.RiassegnaPraticaClient;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.TaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.portlet.consolepec.gwt.server.AbstractCambiaGruppoActionHandler;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.assegna.fasciolo.RiassegnaFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.assegna.fasciolo.RiassegnaFascicoloResult;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CambiaStatoFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CambiaStatoFascicoloEnum;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CambiaStatoFascicoloResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;
import it.eng.portlet.consolepec.spring.bean.configurazioni.GestioneConfigurazioni;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.AggiornaStatoFascicolo;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.GestioneTaskPratiche;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class CambiaGruppoFascicoloActionHandler extends AbstractCambiaGruppoActionHandler<RiassegnaFascicolo, RiassegnaFascicoloResult> {

	Logger logger = LoggerFactory.getLogger(CambiaGruppoFascicoloActionHandler.class);

	@Autowired
	XMLPluginToDTOConverter praticaUtil;
	@Autowired
	PraticaSessionUtil praticaSessionUtil;
	@Autowired
	RiassegnaPraticaClient riassegnaPraticaClient;
	@Autowired
	UserSessionUtil userSessionUtil;
	@Autowired
	GestioneConfigurazioni gestioneConfigurazioni;
	@Autowired
	AggiornaStatoFascicolo aggiornaStatoFascicolo;
	@Autowired
	GestioneTaskPratiche gestioneTaskPratiche;
	@Autowired
	GestioneProfilazioneUtente gestioneProfilazioneUtente;

	public CambiaGruppoFascicoloActionHandler() {}

	@Override
	public RiassegnaFascicoloResult execute(RiassegnaFascicolo action, ExecutionContext context, PreferenzeRiassegnazione preferenzeRiassegnazione) {
		RiassegnaFascicoloResult result = null;
		List<FascicoloDTO> tempFascicoli = new ArrayList<FascicoloDTO>();
		HashSet<FascicoloDTO> tempFascicoli2 = new HashSet<FascicoloDTO>();
		try {
			// recupero tutti i fascicoli
			for (String id : action.getIds()) {
				Fascicolo fascicolo = praticaSessionUtil.loadFascicoloFromEncodedPath(id, TipologiaCaricamento.CARICA);
				FascicoloDTO dto = praticaUtil.fascicoloToDettaglio(fascicolo);
				tempFascicoli.add(dto);
			}

			// eseguo il cambiamento di stato
			if (tempFascicoli.size() == 1 && action.getStato() != null && !tempFascicoli.get(0).getStato().equals(action.getStato())) {
				logger.info("Eseguo il cambiamento di stato del fascicolo");
				CambiaStatoFascicolo cambiaStatoFascicolo = new CambiaStatoFascicolo(action.getIds(), CambiaStatoFascicoloEnum.translator(action.getStato()));
				CambiaStatoFascicoloResult cambiaStato = aggiornaStatoFascicolo.cambiaStato(cambiaStatoFascicolo);
				tempFascicoli.clear();
				tempFascicoli.add(cambiaStato.getFascicoli().get(0));
			}

			// se possibile procedo alla riassegnazione della pratica
			for (FascicoloDTO fascicoloDTO : tempFascicoli) {
				String path = Base64Utils.URLdecodeAlfrescoPath(fascicoloDTO.getClientID());
				logger.debug("Richiesto cambio gruppo fascicolo: {} gruppo:{}", path, preferenzeRiassegnazione.getRuolo());

				Fascicolo fascicolo = praticaSessionUtil.loadFascicoloFromEncodedPath(fascicoloDTO.getClientID(), TipologiaCaricamento.CARICA);

				// controllo che dopo il cambiamento di stato il fascicolo sia riassegnabile
				TaskFascicolo<?> taskCorrente = gestioneTaskPratiche.estraiTaskCorrente(fascicolo, TaskFascicolo.class, gestioneProfilazioneUtente.getDatiUtente().getAnagraficheRuoli());

				if (taskCorrente == null || !taskCorrente.controllaAbilitazione(TipoApiTask.RIASSEGNA)) {
					tempFascicoli2.add(fascicoloDTO);
					continue;
				}

				LockedPratica riassegna = riassegnaPraticaClient.riassegna(path, preferenzeRiassegnazione.getRuolo(), action.getIndirizziNotifica(),
						action.getOperatore() != null ? action.getOperatore() : "", action.getNote() != null ? action.getNote() : "", userSessionUtil.getUtenteSpagic());

				praticaSessionUtil.removePraticaFromEncodedPath(fascicoloDTO.getClientID());
				fascicolo = (Fascicolo) praticaSessionUtil.loadPraticaInSessione(riassegna);

				logger.debug("Caricato fascicolo: {}", fascicolo.getDati());
				FascicoloDTO dto = praticaUtil.fascicoloToDettaglio(fascicolo);
				logger.debug("Riassegnato fascicolo a: {}", dto.getAssegnatario());
				tempFascicoli2.add(dto);

			}

			result = new RiassegnaFascicoloResult(new ArrayList<FascicoloDTO>(tempFascicoli2), null, false);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			result = new RiassegnaFascicoloResult(null, e.getErrorMessage(), true);

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			result = new RiassegnaFascicoloResult(null, ConsolePecConstants.ERROR_MESSAGE, true);

		}

		return result;
	}

	@Override
	public void undo(RiassegnaFascicolo action, RiassegnaFascicoloResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<RiassegnaFascicolo> getActionType() {
		return RiassegnaFascicolo.class;
	}

	@Override
	public GestioneProfilazioneUtente getGestioneProfilazioneUtente() {
		return gestioneProfilazioneUtente;
	}
}
