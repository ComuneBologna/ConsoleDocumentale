package it.eng.portlet.consolepec.spring.bean.gestionepratiche.impl;

import java.util.ArrayList;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import it.eng.consolepec.spagicclient.SpagicClientGestioneFascicolo;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.PraticaCollegata;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Stato;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.TaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.CambiaStatoFascicoloTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.ConcludiFascicoloTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.MettiInAffissioneTaskApi;
import it.eng.consolepec.xmlplugin.tasks.riattiva.RiattivaFascicoloTask;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CambiaStatoFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CambiaStatoFascicoloEnum;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CambiaStatoFascicoloResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.AggiornaStatoFascicolo;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.GestioneTaskPratiche;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class AggiornaStatoFascicoloImpl implements AggiornaStatoFascicolo {

	private static final Logger logger = LoggerFactory.getLogger(AggiornaStatoFascicoloImpl.class);

	@Autowired
	XMLPluginToDTOConverter praticaUtil;

	@Autowired
	PraticaSessionUtil praticaSessionUtil;

	@Autowired
	SpagicClientGestioneFascicolo spagicClientGestioneFascicolo;

	@Autowired
	UserSessionUtil userSessionUtil;

	@Autowired
	GestioneTaskPratiche gestioneTask;

	@Autowired
	GestioneProfilazioneUtente gestioneProfilazioneUtente;

	@Override
	public CambiaStatoFascicoloResult cambiaStato(CambiaStatoFascicolo cambiaStatoFascicolo) {
		CambiaStatoFascicoloEnum azione = cambiaStatoFascicolo.getStato();
		CambiaStatoFascicoloResult res = null;

		switch (azione) {
		case ARCHIVIA:
			res = archivia(cambiaStatoFascicolo.getIds());
			break;
		case RIPORTAINGESTIONE:
			res = riportaInGestione(cambiaStatoFascicolo.getIds());
			break;
		case ELIMINATO:
			res = elimina(cambiaStatoFascicolo.getIds());
			break;
		case IN_AFFISSIONE:
			res = mettiInAffissione(cambiaStatoFascicolo.getIds());
			break;
		case IN_VISIONE:
			res = mettiInVisione(cambiaStatoFascicolo.getIds());
			break;
		default:
			break;

		}
		return res;
	}

	private CambiaStatoFascicoloResult mettiInAffissione(Set<String> ids) {
		CambiaStatoFascicoloResult res = new CambiaStatoFascicoloResult(null);
		try {
			for (String id : ids) {
				Fascicolo fascicolo = praticaSessionUtil.loadFascicoloFromEncodedPath(id, TipologiaCaricamento.RICARICA);
				fascicolo.getDati().setLetto(true);
				TaskFascicolo<?> task = gestioneTask.estraiTaskCorrente(fascicolo, TaskFascicolo.class, gestioneProfilazioneUtente.getDatiUtente().getAnagraficheRuoli());
				logger.debug("Metto il fascicolo in affissione");
				((MettiInAffissioneTaskApi) task).mettiInAffissione();
				logger.debug("Persistenza del fascicolo");
				praticaSessionUtil.updateFascicolo(fascicolo);
				FascicoloDTO dto = praticaUtil.fascicoloToDettaglio(fascicolo);
				res.addFascicolo(dto);
				praticaSessionUtil.removePraticaFromEncodedPath(id);
				for (PraticaCollegata p : fascicolo.getAllPraticheCollegate()) {
					praticaSessionUtil.removePraticaFromEncodedPath(Base64Utils.URLencodeAlfrescoPath(p.getAlfrescoPath()));
				}
			}

		} catch (SpagicClientException e) {
			logger.error("Errore durante il cambio stato in affissione", e);
			res = new CambiaStatoFascicoloResult(null, true, e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore durante il cambio stato in affissione", e);
			res = new CambiaStatoFascicoloResult(null, true, ConsolePecConstants.ERROR_MESSAGE);
		}

		return res;
	}

	private CambiaStatoFascicoloResult mettiInVisione(Set<String> ids) {
		CambiaStatoFascicoloResult res = new CambiaStatoFascicoloResult(null);
		try {
			for (String id : ids) {
				Fascicolo fascicolo = praticaSessionUtil.loadFascicoloFromEncodedPath(id, TipologiaCaricamento.RICARICA);
				fascicolo.getDati().setLetto(true);
				TaskFascicolo<?> task = gestioneTask.estraiTaskCorrente(fascicolo, TaskFascicolo.class, gestioneProfilazioneUtente.getDatiUtente().getAnagraficheRuoli());
				logger.debug("Metto il fascicolo in visone");
				((CambiaStatoFascicoloTaskApi) task).cambiaStato(Stato.IN_VISIONE);
				logger.debug("Persistenza del fascicolo");
				praticaSessionUtil.updateFascicolo(fascicolo);
				FascicoloDTO dto = praticaUtil.fascicoloToDettaglio(fascicolo);
				res.addFascicolo(dto);
				praticaSessionUtil.removePraticaFromEncodedPath(id);
				for (PraticaCollegata p : fascicolo.getAllPraticheCollegate()) {
					praticaSessionUtil.removePraticaFromEncodedPath(Base64Utils.URLencodeAlfrescoPath(p.getAlfrescoPath()));
				}
			}
		} catch (SpagicClientException e) {
			logger.error("Errore durante il cambio stato in visione", e);
			res = new CambiaStatoFascicoloResult(null, true, e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore durante il cambio stato in visione", e);
			res = new CambiaStatoFascicoloResult(null, true, ConsolePecConstants.ERROR_MESSAGE);
		}

		return res;
	}

	private CambiaStatoFascicoloResult riportaInGestione(Set<String> ids) {
		CambiaStatoFascicoloResult res = new CambiaStatoFascicoloResult(null, false, "");
		try {
			for (String id : ids) {
				Fascicolo fascicolo = praticaSessionUtil.loadFascicoloFromEncodedPath(id, TipologiaCaricamento.RICARICA);
				fascicolo.getDati().setLetto(true);
				if (fascicolo.getDati().getStato().equals(Stato.ARCHIVIATO) || fascicolo.getDati().getStato().equals(Stato.IN_AFFISSIONE) || fascicolo.getDati().getStato().equals(Stato.ANNULLATO)) {
					RiattivaFascicoloTask task = gestioneTask.getRiattivaFascicoloTask(fascicolo);
					logger.debug("riporta  il fascicolo in gestione");
					task.riattivaTaskAssociato();
				} else {
					TaskFascicolo<?> task = gestioneTask.estraiTaskCorrente(fascicolo, TaskFascicolo.class, gestioneProfilazioneUtente.getDatiUtente().getAnagraficheRuoli());
					logger.debug("Metto il fascicolo in gestione");
					((CambiaStatoFascicoloTaskApi) task).cambiaStato(Stato.IN_GESTIONE);
				}
				logger.debug("Persistenza del fascicolo");
				praticaSessionUtil.updateFascicolo(fascicolo);
				FascicoloDTO dto = praticaUtil.fascicoloToDettaglio(fascicolo);
				res.addFascicolo(dto);
				praticaSessionUtil.removePraticaFromEncodedPath(id);
				for (PraticaCollegata p : fascicolo.getAllPraticheCollegate()) {
					praticaSessionUtil.removePraticaFromEncodedPath(Base64Utils.URLencodeAlfrescoPath(p.getAlfrescoPath()));
				}
			}
		} catch (SpagicClientException e) {
			logger.error("Errore durante il riporta in gestione", e);
			res = new CambiaStatoFascicoloResult(null, true, e.getErrorMessage());

		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			res = new CambiaStatoFascicoloResult(null, true, ConsolePecConstants.ERROR_MESSAGE);
		}

		return res;
	}

	private CambiaStatoFascicoloResult archivia(Set<String> ids) {
		CambiaStatoFascicoloResult res = new CambiaStatoFascicoloResult(null, false, "");
		try {
			for (String id : ids) {
				Fascicolo fascicolo = praticaSessionUtil.loadFascicoloFromEncodedPath(id, TipologiaCaricamento.RICARICA);
				fascicolo.getDati().setLetto(true);
				TaskFascicolo<?> task = gestioneTask.estraiTaskCorrente(fascicolo, TaskFascicolo.class, gestioneProfilazioneUtente.getDatiUtente().getAnagraficheRuoli());
				logger.debug("Concludo il fascicolo");
				((ConcludiFascicoloTaskApi) task).concludiFascicolo();
				logger.debug("Persistenza del fascicolo");
				praticaSessionUtil.updateFascicolo(fascicolo);
				FascicoloDTO dto = praticaUtil.fascicoloToDettaglio(fascicolo);
				res.addFascicolo(dto);
				praticaSessionUtil.removePraticaFromEncodedPath(id);
				for (PraticaCollegata p : fascicolo.getAllPraticheCollegate()) {
					praticaSessionUtil.removePraticaFromEncodedPath(Base64Utils.URLencodeAlfrescoPath(p.getAlfrescoPath()));
				}
			}

		} catch (SpagicClientException e) {
			logger.error("Errore durante l'archiviazione", e);
			res = new CambiaStatoFascicoloResult(null, true, e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore durante l'archiviazione", e);
			res = new CambiaStatoFascicoloResult(null, true, ConsolePecConstants.ERROR_MESSAGE);
		}

		return res;
	}

	private CambiaStatoFascicoloResult elimina(Set<String> ids) {
		CambiaStatoFascicoloResult res = new CambiaStatoFascicoloResult(null, false, "");
		ArrayList<String> clientIdEliminati = new ArrayList<String>();
		try {
			for (String id : ids) {
				Fascicolo fascicolo = praticaSessionUtil.loadFascicoloFromEncodedPath(id, TipologiaCaricamento.RICARICA);
				logger.debug("Elimino il fascicolo");

				fascicolo.getDati().setLetto(true);

				boolean eliminato = spagicClientGestioneFascicolo.elimina(fascicolo.getAlfrescoPath(), userSessionUtil.getUtenteSpagic());

				if (eliminato) {
					clientIdEliminati.add(id);
				}
				praticaSessionUtil.removePraticaFromEncodedPath(id);
				for (PraticaCollegata p : fascicolo.getAllPraticheCollegate()) {
					praticaSessionUtil.removePraticaFromEncodedPath(Base64Utils.URLencodeAlfrescoPath(p.getAlfrescoPath()));
				}
			}
		} catch (SpagicClientException e) {
			logger.error("Errore durante l'eliminazione", e);
			res = new CambiaStatoFascicoloResult(null, true, e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore durante l'eliminazione", e);
			res = new CambiaStatoFascicoloResult(null, true, ConsolePecConstants.ERROR_MESSAGE);
		}

		res.setClientIdEliminati(clientIdEliminati);
		return res;

	}

}
