package it.eng.portlet.consolepec.spring.bean.gestionepratiche.impl;

import java.util.ArrayList;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import it.eng.consolepec.spagicclient.SpagicClientGestionePraticaModulistica;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.xmlplugin.pratica.modulistica.PraticaModulistica;
import it.eng.consolepec.xmlplugin.tasks.gestionemodulistica.GestionePraticaModulisticaTask;
import it.eng.consolepec.xmlplugin.tasks.riattiva.RiattivaPraticaModulisticaTask;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.modulistica.CambiaStatoPraticaModulistica;
import it.eng.portlet.consolepec.gwt.shared.action.modulistica.CambiaStatoPraticaModulisticaEnum;
import it.eng.portlet.consolepec.gwt.shared.action.modulistica.CambiaStatoPraticaModulisticaResult;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.AggiornaStatoPraticaModulistica;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.GestioneTaskPratiche;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class AggiornaStatoPraticaModulisticaImpl implements AggiornaStatoPraticaModulistica {

	Logger logger = LoggerFactory.getLogger(AggiornaStatoPraticaModulisticaImpl.class);

	@Autowired
	XMLPluginToDTOConverter praticaUtil;
	@Autowired
	PraticaSessionUtil praticaSessionUtil;
	@Autowired
	SpagicClientGestionePraticaModulistica spagicClientGestionePraticaModulistica;
	@Autowired
	UserSessionUtil userSessionUtil;
	@Autowired
	GestioneTaskPratiche gestioneTask;
	@Autowired
	GestioneProfilazioneUtente gestioneProfilazioneUtente;

	public AggiornaStatoPraticaModulisticaImpl() {}

	private CambiaStatoPraticaModulisticaResult riportaInGestione(Set<String> ids) {
		CambiaStatoPraticaModulisticaResult res = new CambiaStatoPraticaModulisticaResult(null, false, "");
		try {
			for (String id : ids) {

				PraticaModulistica praticaModulistica = praticaSessionUtil.loadPraticaModulisticaFromEncodedPath(id, TipologiaCaricamento.RICARICA);
				praticaModulistica.getDati().setLetto(true);

				RiattivaPraticaModulisticaTask task = gestioneTask.getRiattivaPraticaModulisticaTask(praticaModulistica);
				logger.debug("riattiva la pratica modulistica");
				task.riattivaTaskAssociato();
				logger.debug("Persistenza della pratica modulistica");

				praticaSessionUtil.updatePraticaModulistica(praticaModulistica);
				PraticaModulisticaDTO dto = praticaUtil.praticaModulisticaToDettaglio(praticaModulistica);
				res.setPraticaModulistica(dto);
				praticaSessionUtil.removePraticaFromEncodedPath(id);

			}
		} catch (SpagicClientException e) {
			logger.error("Errore durante il riporta in gestione", e);
			res = new CambiaStatoPraticaModulisticaResult(null, true, e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore durante il riporta in gestione", e);
			res = new CambiaStatoPraticaModulisticaResult(null, true, ConsolePecConstants.ERROR_MESSAGE);
		}
		return res;
	}

	private CambiaStatoPraticaModulisticaResult archivia(Set<String> ids) {
		CambiaStatoPraticaModulisticaResult res = new CambiaStatoPraticaModulisticaResult(null, false, "");
		try {
			for (String id : ids) {
				PraticaModulistica praticaModulistica = praticaSessionUtil.loadPraticaModulisticaFromEncodedPath(id, TipologiaCaricamento.RICARICA);
				praticaModulistica.getDati().setLetto(true);

				GestionePraticaModulisticaTask task = gestioneTask.estraiTaskCorrente(praticaModulistica, GestionePraticaModulisticaTask.class,
						gestioneProfilazioneUtente.getDatiUtente().getAnagraficheRuoli());
				logger.debug("Concludo la pratica modulistica");

				task.archivia();

				logger.debug("Persistenza della pratica modulistica");

				praticaSessionUtil.updatePraticaModulistica(praticaModulistica);
				PraticaModulisticaDTO dto = praticaUtil.praticaModulisticaToDettaglio(praticaModulistica);
				res.setPraticaModulistica(dto);
				praticaSessionUtil.removePraticaFromEncodedPath(id);

			}
		} catch (SpagicClientException e) {
			logger.error("Errore durante l'archiviazione", e);
			res = new CambiaStatoPraticaModulisticaResult(null, true, e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore durante l'archiviazione", e);
			res = new CambiaStatoPraticaModulisticaResult(null, true, ConsolePecConstants.ERROR_MESSAGE);
		}

		return res;
	}

	private CambiaStatoPraticaModulisticaResult elimina(Set<String> ids) {
		CambiaStatoPraticaModulisticaResult res = new CambiaStatoPraticaModulisticaResult(null, false, "");
		ArrayList<String> clientIdEliminati = new ArrayList<String>();
		try {
			for (String id : ids) {
				PraticaModulistica praticaModulistica = praticaSessionUtil.loadPraticaModulisticaFromEncodedPath(id, TipologiaCaricamento.RICARICA);
				logger.debug("Elimino la pratica modulistica");

				praticaModulistica.getDati().setLetto(true);

				boolean eliminato = spagicClientGestionePraticaModulistica.elimina(praticaModulistica.getAlfrescoPath(), userSessionUtil.getUtenteSpagic());

				if (eliminato) {
					clientIdEliminati.add(id);
				}
				praticaSessionUtil.removePraticaFromEncodedPath(id);

			}
		} catch (SpagicClientException e) {
			logger.error("Errore durante l'eliminazione", e);
			res = new CambiaStatoPraticaModulisticaResult(null, true, e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore durante l'eliminazione", e);
			res = new CambiaStatoPraticaModulisticaResult(null, true, ConsolePecConstants.ERROR_MESSAGE);
		}
		res.setClientIdEliminati(clientIdEliminati);
		return res;

	}

	@Override
	public CambiaStatoPraticaModulisticaResult cambiaStato(CambiaStatoPraticaModulistica action) {
		CambiaStatoPraticaModulisticaEnum azione = action.getStato();
		CambiaStatoPraticaModulisticaResult res = null;

		switch (azione) {
		case ARCHIVIA:
			res = archivia(action.getIds());
			break;
		case RIPORTAINGESTIONE:
			res = riportaInGestione(action.getIds());
			break;
		case ELIMINATA:
			res = elimina(action.getIds());
			break;
		case RILASCIA_IN_CARICO:
			res = rilasciaInCarico(action.getIds());
			break;
		default:
			break;

		}
		return res;
	}

	private CambiaStatoPraticaModulisticaResult rilasciaInCarico(Set<String> ids) {
		CambiaStatoPraticaModulisticaResult res = new CambiaStatoPraticaModulisticaResult(null, false, "");
		try {
			for (final String id : ids) {
				PraticaModulistica praticaModulistica = praticaSessionUtil.loadPraticaModulisticaFromEncodedPath(id, TipologiaCaricamento.RICARICA);
				logger.debug("Eseguo operazione di riporta in gestione su pratica modulistica");

				praticaModulistica.getDati().setLetto(true);
				GestionePraticaModulisticaTask task = gestioneTask.estraiTaskCorrente(praticaModulistica, GestionePraticaModulisticaTask.class,
						gestioneProfilazioneUtente.getDatiUtente().getAnagraficheRuoli());
				if (praticaModulistica.getInCaricoA() != null) {
					task.rilasciaInCarico(userSessionUtil.getUtentePratica());
				}

				praticaSessionUtil.updatePraticaModulistica(praticaModulistica);
				PraticaModulisticaDTO dto = praticaUtil.praticaModulisticaToDettaglio(praticaModulistica);
				res.setPraticaModulistica(dto);
			}
		} catch (SpagicClientException e) {
			logger.error("Errore durante il rilascio in carico", e);
			res = new CambiaStatoPraticaModulisticaResult(null, true, e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore durante il rilascio in carico", e);
			res = new CambiaStatoPraticaModulisticaResult(null, true, ConsolePecConstants.ERROR_MESSAGE);
		}
		return res;
	}
}
