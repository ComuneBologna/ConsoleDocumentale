package it.eng.portlet.consolepec.spring.bean.gestionepratiche.impl;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import it.eng.cobo.consolepec.util.utente.UtenteUtils;
import it.eng.consolepec.client.PresaInCaricoClient;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.PraticaCollegata;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Utente;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO.TipoPresaInCarico;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.IGestionePresaInCarico;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class GestionePresaInCarico implements IGestionePresaInCarico {

	@Autowired
	GestioneProfilazioneUtente gestioneProfilazioneUtente;

	@Autowired
	PresaInCaricoClient presaInCaricoClient;

	@Autowired
	PraticaSessionUtil praticaSessionUtil;

	@Autowired
	UserSessionUtil userSessionUtil;

	private static final Logger logger = LoggerFactory.getLogger(GestionePresaInCarico.class);

	public enum TipoPresoInCarica {
		FASCICOLO, PRATICA
	};

	@Override
	public Pratica<?> prendiInCarico(String id) {
		try {

			Pratica<?> pratica = praticaSessionUtil.loadPraticaFromEncodedPath(id);

			if (pratica.getInCaricoA() == null || pratica.getInCaricoA().getUsername().equals(gestioneProfilazioneUtente.getDatiUtente().getUsername())) {
				LockedPratica praticaLock = presaInCaricoClient.prendiInCarico(pratica.getDati().getIdDocumentale(), userSessionUtil.getUtenteSpagic());
				pratica = praticaSessionUtil.loadPraticaInSessione(praticaLock);
				logger.debug("La pratica: " + pratica.getAlfrescoPath() + " + è stato preso in carico carico dall'utente " + gestioneProfilazioneUtente.getDatiUtente().getUsername());

				// rimuovo dalla sessione tutte le pratiche collegate del
				// fascicolo

				if (pratica instanceof Fascicolo) {
					Fascicolo fascicolo = (Fascicolo) pratica;
					List<PraticaCollegata> praticheCollegate = fascicolo.getAllPraticheCollegate();
					for (PraticaCollegata praticaCollegata : praticheCollegate) {
						praticaSessionUtil.removePraticaFromEncodedPath(Base64Utils.URLencodeAlfrescoPath(praticaCollegata.getAlfrescoPath()));
					}
				}
				return pratica;

			} else {
				throw new RuntimeException("La pratica: " + pratica.getAlfrescoPath() + " + è in carico all'utente " + gestioneProfilazioneUtente.getDatiUtente().getUsername());
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}

	}

	@Override
	public Pratica<?> rilascia(String id) {
		try {
			Pratica<?> pratica = praticaSessionUtil.loadPraticaFromEncodedPath(id);

			if (pratica.getInCaricoA() != null && pratica.getInCaricoA().getUsername().equals(gestioneProfilazioneUtente.getDatiUtente().getUsername().toLowerCase())) {
				LockedPratica praticaLock = presaInCaricoClient.rilasciaInCarico(pratica.getDati().getIdDocumentale(), userSessionUtil.getUtenteSpagic());
				pratica = praticaSessionUtil.loadPraticaInSessione(praticaLock);
				logger.debug("La pratica: " + pratica.getAlfrescoPath() + " + è stato rilasciato dall'utente " + gestioneProfilazioneUtente.getDatiUtente().getUsername());
				return pratica;
			} else {
				if (pratica.getInCaricoA() == null) {
					throw new RuntimeException("La pratica: " + pratica.getAlfrescoPath() + " + non può essere rilasciato in quanto non è incarico a nessun utente ");
				} else
					throw new RuntimeException("La pratica: " + pratica.getAlfrescoPath() + " + è in carico all'utente " + pratica.getInCaricoA());
			}
		} catch (Exception e) {
			logger.error("Errore nel rilascio della pratica.", e);
			throw new RuntimeException(e.getMessage());
		}

	}

	@Override
	public void caricaPresaInCarico(Pratica<?> pratica, PraticaDTO praticaDTO) {
		Utente inCaricoA = pratica.getDati().getInCaricoA();
		if (inCaricoA != null) {
			praticaDTO.setInCaricoALabel(getInCaricoALabel(inCaricoA));
			praticaDTO.setInCaricoAUserName(inCaricoA.getUsername());
		}
		caricaPresaInCarico(praticaDTO);
	}

	@Override
	public void caricaPresaInCarico(PraticaDTO praticaDTO) {
		TipoPresaInCarico tipoPresaInCarico = null;
		if (praticaDTO.getInCaricoAUserName() == null) {
			tipoPresaInCarico = TipoPresaInCarico.NESSUNO;
		} else {
			tipoPresaInCarico = gestioneProfilazioneUtente.getDatiUtente().getUsername().equalsIgnoreCase(praticaDTO.getInCaricoAUserName()) ? TipoPresaInCarico.UTENTE_CORRENTE
					: TipoPresaInCarico.ALTRO_UTENTE;
		}
		praticaDTO.setTipoPresaInCarico(tipoPresaInCarico);
	}

	@Override
	public boolean hasInCaricoUtenteCorrente(String id) {
		Pratica<?> pratica;
		try {
			pratica = praticaSessionUtil.loadPraticaFromEncodedPath(id);
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return pratica.getInCaricoA() != null && pratica.getInCaricoA().getUsername().equalsIgnoreCase(gestioneProfilazioneUtente.getDatiUtente().getUsername());
	}

	@Override
	public String getInCaricoALabel(Utente utente) {
		if (utente.getNome() != null && utente.getCognome() != null && (utente.getNome() + utente.getCognome()).trim().length() > 0) {

			StringBuilder sb = new StringBuilder();
			sb.append(utente.getNome()).append(" ").append(utente.getCognome()).append(" - ");
			if (UtenteUtils.MATRICOLA_UTENTI_NON_COMUNALI.equals(utente.getMatricola()))
				sb.append(utente.getCodicefiscale());
			else
				sb.append(utente.getMatricola());
			return sb.toString();
		}
		return null;
	}

}
