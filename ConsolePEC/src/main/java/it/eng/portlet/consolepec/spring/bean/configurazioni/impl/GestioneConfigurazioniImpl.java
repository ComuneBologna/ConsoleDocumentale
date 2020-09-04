package it.eng.portlet.consolepec.spring.bean.configurazioni.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import it.eng.cobo.consolepec.commons.configurazioni.AbilitazioniRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaComunicazione;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaEmailOut;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaIngresso;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaModello;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaPraticaModulistica;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo.Stato;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.Abilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.CartellaFirmaAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.CreazioneFascicoloAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.LetturaIngressoAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.ModificaRuoloAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.RuoloAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.VisibilitaPraticaAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.VisibilitaRuoloAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.properties.ProprietaGenerali;
import it.eng.cobo.consolepec.commons.exception.InvalidArgumentException;
import it.eng.cobo.consolepec.commons.profilazione.Settore;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.consolepec.client.ConfigurazioniClient;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.spring.bean.configurazioni.GestioneConfigurazioni;

/**
 *
 * Il bean viene invocato attraverso il suo init-method all'avvio del server
 *
 * @author biagiot
 *
 */
public class GestioneConfigurazioniImpl implements GestioneConfigurazioni {

	private static final Logger logger = LoggerFactory.getLogger(GestioneConfigurazioniImpl.class);

	private ReadWriteLock lock = new ReentrantReadWriteLock();

	@Autowired
	ConfigurazioniClient configurazioniClient;

	private ProprietaGenerali proprietaGenerali;
	private List<AnagraficaFascicolo> anagraficheFascicoli = new ArrayList<AnagraficaFascicolo>();
	private List<AnagraficaIngresso> anagraficheIngressi = new ArrayList<AnagraficaIngresso>();
	private List<AnagraficaModello> anagraficheModelli = new ArrayList<AnagraficaModello>();
	private List<AnagraficaRuolo> anagraficheRuoli = new ArrayList<AnagraficaRuolo>();
	private List<AnagraficaEmailOut> anagraficheMailInUscita = new ArrayList<AnagraficaEmailOut>();
	private List<AnagraficaComunicazione> anagraficheComunicazione = new ArrayList<AnagraficaComunicazione>();
	private List<AnagraficaPraticaModulistica> anagrafichePraticaModulistica = new ArrayList<AnagraficaPraticaModulistica>();
	private List<Settore> settori = new ArrayList<Settore>();
	private List<AnagraficaRuolo> ruoliPersonali = new ArrayList<AnagraficaRuolo>();
	private List<AbilitazioniRuolo> abilitazioniRuoli = new ArrayList<AbilitazioniRuolo>();

	private String baseUrlPubblicazioneAllegato;

	private String adminUsername;
	private String adminNome;
	private String adminCognome;
	private String adminRuolo;
	private String adminMatricola;
	private String adminCodiceFiscale;
	private Utente consoleAdminUser;

	private Date lastEditDate;

	/**
	 * init-method
	 */
	@Override
	public void init() {

		lock.writeLock().lock();
		consoleAdminUser = new Utente(adminNome, adminCognome, adminUsername, adminMatricola, adminCodiceFiscale, Arrays.asList(adminRuolo));

		try {
			caricaAnagrafichePratiche(consoleAdminUser);
			caricaAnagraficheRuoli(consoleAdminUser);
			caricaSettori(consoleAdminUser);
			caricaProprietaGenerali(consoleAdminUser);
			caricaAbilitazioniRuoli(consoleAdminUser);
			this.lastEditDate = new Date();

		} catch (Exception e) {
			logger.info("Errore durante l'inizializzazione del gestore configurazioni", e);

		} finally {
			lock.writeLock().unlock();
		}

	}

	@Override
	public void reload(Utente utente) throws SpagicClientException {

		if (this.lastEditDate == null) {
			this.init();
			return;
		}

		try {
			Date date = configurazioniClient.getLastEditDate(utente);
			logger.debug("Ultima data di modifica configurazioni: {}", date);

			if (date == null) {
				logger.error("Risposta nulla ricevuta dai servizi");
				return;
			}

			if (this.lastEditDate.before(date)) {
				this.init();
			}

		} catch (Exception e) {
			logger.error("Errore durante il recupero dell'ultima data di modifica delle configurazioni", e);
		}

	}

	@Override
	public void reloadRuoli(Utente utente) {
		lock.writeLock().lock();

		try {
			caricaAnagraficheRuoli(utente);
			caricaAbilitazioniRuoli(utente);
			caricaSettori(utente);
			this.lastEditDate = new Date();

		} finally {
			lock.writeLock().unlock();

		}

	}

	@Override
	public void reloadAnagraficheFascicoli(Utente utente) {

		lock.writeLock().lock();

		try {
			this.anagraficheFascicoli.clear();
			this.anagraficheFascicoli = configurazioniClient.getAnagraficheFascicoli(utente);
			this.anagraficheFascicoli.removeAll(Collections.singleton(null));
			this.lastEditDate = new Date();

		} finally {
			lock.writeLock().unlock();

		}
	}

	@Override
	public void reloadAnagraficheIngressi(Utente utente) {

		lock.writeLock().lock();

		try {
			this.anagraficheIngressi.clear();
			this.anagraficheIngressi = configurazioniClient.getAnagraficheIngressi(utente);
			this.anagraficheIngressi.removeAll(Collections.singleton(null));

			this.anagraficheMailInUscita.clear();
			this.anagraficheMailInUscita = configurazioniClient.getAnagraficheMailInUscita(utente);
			this.anagraficheMailInUscita.removeAll(Collections.singleton(null));

			this.lastEditDate = new Date();

		} finally {
			lock.writeLock().unlock();

		}

	}

	@Override
	public void reloadAbilitazioniRuoli(Utente utente) {

		lock.writeLock().lock();

		try {
			caricaAbilitazioniRuoli(utente);
			this.lastEditDate = new Date();

		} finally {
			lock.writeLock().unlock();
		}

	}

	@Override
	public void addRuoloPersonale(Utente utente) {
		AnagraficaRuolo ruoloPersonale = utente.getRuoloPersonale();

		if (!ruoliPersonali.contains(ruoloPersonale)) {

			lock.writeLock().lock();

			try {
				ruoliPersonali.add(ruoloPersonale);

			} finally {
				lock.writeLock().unlock();
			}
		}
	}

	@Override
	public List<AnagraficaRuolo> getAnagraficheRuoli() {

		lock.readLock().lock();

		try {
			return new ArrayList<>(this.anagraficheRuoli);

		} finally {
			lock.readLock().unlock();

		}

	}

	@Override
	public List<Settore> getSettori() {

		lock.readLock().lock();

		try {
			return new ArrayList<>(this.settori);

		} finally {
			lock.readLock().unlock();

		}
	}

	@Override
	public ProprietaGenerali getProprietaGenerali() {

		lock.readLock().lock();

		try {
			return this.proprietaGenerali;

		} finally {
			lock.readLock().unlock();

		}

	}

	@Override
	public AnagraficaRuolo getAnagraficaRuoloByEtichetta(String etichetta) {

		for (AnagraficaRuolo ar : getAnagraficheRuoli()) {
			if (ar.getEtichetta().equals(etichetta)) {
				return ar;
			}
		}

		for (AnagraficaRuolo ar : getAnagraficheRuoliPersonali()) {
			if (ar.getEtichetta().equals(etichetta)) {
				return ar;
			}
		}

		return null;
	}

	@Override
	public AnagraficaRuolo getAnagraficaRuolo(String ruolo) {
		return getAnagraficaRuolo(ruolo, true);
	}

	@Override
	public AnagraficaRuolo getAnagraficaRuolo(String ruolo, boolean checkRuoliPersonali) {

		for (AnagraficaRuolo ar : getAnagraficheRuoli()) {
			if (ar.getRuolo().equals(ruolo)) {
				return ar;
			}
		}

		if (checkRuoliPersonali) {
			for (AnagraficaRuolo ar : getAnagraficheRuoliPersonali()) {
				if (ar.getRuolo().equals(ruolo)) {
					return ar;
				}
			}
		}

		return null;
	}

	@Override
	public List<AnagraficaFascicolo> getAnagraficheFascicoli() {

		lock.readLock().lock();

		try {
			return new ArrayList<>(anagraficheFascicoli);

		} finally {
			lock.readLock().unlock();

		}

	}

	@Override
	public List<AnagraficaIngresso> getAnagraficheIngressi() {

		lock.readLock().lock();

		try {
			return new ArrayList<>(anagraficheIngressi);

		} finally {
			lock.readLock().unlock();
		}

	}

	@Override
	public List<AnagraficaModello> getAnagraficheModelli() {

		lock.readLock().lock();

		try {
			return new ArrayList<>(anagraficheModelli);

		} finally {
			lock.readLock().unlock();

		}

	}

	@Override
	public List<AnagraficaEmailOut> getAnagraficheMailInUscita() {

		lock.readLock().lock();

		try {
			return new ArrayList<>(anagraficheMailInUscita);

		} finally {
			lock.readLock().unlock();

		}
	}

	@Override
	public List<AnagraficaComunicazione> getAnagraficheComunicazioni() {

		lock.readLock().lock();

		try {
			return new ArrayList<>(anagraficheComunicazione);

		} finally {
			lock.readLock().unlock();

		}
	}

	@Override
	public List<AnagraficaPraticaModulistica> getAnagrafichePraticaModulistica() {

		lock.readLock().lock();

		try {
			return new ArrayList<>(anagrafichePraticaModulistica);

		} finally {
			lock.readLock().unlock();

		}

	}

	@Override
	public AnagraficaFascicolo getAnagraficaFascicolo(String nomeTipologia) {

		for (AnagraficaFascicolo af : getAnagraficheFascicoli()) {
			if (af.getNomeTipologia().equals(nomeTipologia)) {
				return af;
			}
		}

		return null;
	}

	@Override
	public AnagraficaFascicolo getAnagraficaFascicoloByEtichetta(String etichetta) {

		for (AnagraficaFascicolo af : getAnagraficheFascicoli()) {
			if (af.getEtichettaTipologia().equals(etichetta)) {
				return af;
			}
		}

		return null;
	}

	@Override
	public AnagraficaModello getAnagraficaModello(String tipologia) {

		for (AnagraficaModello af : getAnagraficheModelli()) {
			if (af.getNomeTipologia().equals(tipologia)) {
				return af;
			}
		}

		return null;
	}

	@Override
	public AnagraficaComunicazione getAnagraficaComunicazione(String nomeTipologia) {

		for (AnagraficaComunicazione af : getAnagraficheComunicazioni()) {
			if (af.getNomeTipologia().equals(nomeTipologia)) {
				return af;
			}
		}

		return null;
	}

	@Override
	public AnagraficaPraticaModulistica getAnagraficaPraticaModulistica(String nomeTipologia) {

		for (AnagraficaPraticaModulistica af : getAnagrafichePraticaModulistica()) {
			if (af.getNomeTipologia().equals(nomeTipologia)) {
				return af;
			}
		}

		return null;
	}

	@Override
	public AnagraficaIngresso getAnagraficaIngresso(String tipologia, String indirizzo) {

		for (AnagraficaIngresso af : getAnagraficheIngressi()) {
			if (af.getNomeTipologia().equals(tipologia) && af.getIndirizzo().equalsIgnoreCase(indirizzo)) {
				return af;
			}
		}

		return null;
	}

	@Override
	public AnagraficaEmailOut getAnagraficaMailInUscita(String tipologia, String indirizzo) {

		for (AnagraficaEmailOut af : getAnagraficheMailInUscita()) {
			if (af.getNomeTipologia().equals(tipologia) && af.getIndirizzo().equalsIgnoreCase(indirizzo)) {
				return af;
			}
		}

		return null;
	}

	@Override
	public List<AnagraficaRuolo> getAnagraficheRuoli(Utente utente, boolean checkAttivo) {
		List<AnagraficaRuolo> res = new ArrayList<AnagraficaRuolo>();
		res.add(utente.getRuoloPersonale());

		for (AnagraficaRuolo ar : getAnagraficheRuoli()) {
			if ((!checkAttivo || (checkAttivo && Stato.ATTIVA.equals(ar.getStato()))) && utente.getRuoli().contains(ar.getRuolo())) {
				res.add(ar);
			}
		}

		return res;
	}

	@Override
	public List<AnagraficaRuolo> getAnagraficheRuoliPersonali() {

		lock.readLock().lock();

		try {
			return new ArrayList<>(ruoliPersonali);

		} finally {
			lock.readLock().unlock();

		}

	}

	@Override
	public List<AbilitazioniRuolo> getAbilitazioniRuoli() {

		lock.readLock().lock();

		try {
			return new ArrayList<>(abilitazioniRuoli);

		} finally {
			lock.readLock().unlock();

		}

	}

	@Value("#{portlet['baseUrlPubblicazioneAllegato']}")
	public void setBaseUrlPubblicazioneAllegato(String baseUrlPubblicazioneAllegato) {
		this.baseUrlPubblicazioneAllegato = baseUrlPubblicazioneAllegato;
	}

	@Override
	public String getBaseUrlPubblicazioneAllegati() {
		return baseUrlPubblicazioneAllegato;
	}

	@Override
	public List<String> getEmailAssegnaEsterno() {
		return configurazioniClient.getEmailAssegnaEsterno(consoleAdminUser);
	}

	@Override
	public boolean isCartellaFirmaRiassegnabile(AnagraficaRuolo anagraficaRuolo) {
		boolean riassegnabile = true;

		for (AbilitazioniRuolo ar : getAbilitazioniRuoli()) {
			if (ar.getRuolo().equals(anagraficaRuolo.getRuolo())) {
				for (Abilitazione ab : ar.getAbilitazioni()) {
					if (ab instanceof CartellaFirmaAbilitazione) {
						riassegnabile &= ((CartellaFirmaAbilitazione) ab).isRiassegnabilePerRuolo();
					}
				}
			}
		}

		return riassegnabile;
	}

	@Override
	public List<AnagraficaRuolo> getRuoliVisibilita(TipologiaPratica tipoPratica) throws InvalidArgumentException {

		if (!PraticaUtil.isFascicolo(tipoPratica)) {
			throw new InvalidArgumentException("Tipologia " + tipoPratica + " non supportata", true);
		}

		List<AnagraficaRuolo> res = new ArrayList<>();

		for (AbilitazioniRuolo ar : getAbilitazioniRuoli()) {
			for (Abilitazione a : ar.getAbilitazioni()) {
				if (a.getClass().equals(CreazioneFascicoloAbilitazione.class)) {
					CreazioneFascicoloAbilitazione ia = (CreazioneFascicoloAbilitazione) a;
					if (ia.getTipo().equals(tipoPratica.getNomeTipologia())) {
						AnagraficaRuolo ruolo = getAnagraficaRuolo(ar.getRuolo());
						if (ruolo != null && !res.contains(ruolo) && Stato.ATTIVA.equals(ruolo.getStato())) {
							res.add(ruolo);
						}
					}
				}
			}
		}

		return res;
	}

	@Override
	public AbilitazioniRuolo getAbilitazioniRuolo(String ruolo) {
		for (AbilitazioniRuolo abr : getAbilitazioniRuoli()) {
			if (abr.getRuolo().equals(ruolo)) {
				return abr;
			}
		}
		return null;
	}

	@Override
	public List<AnagraficaRuolo> getRuoliSuperutentiModifica(String role) {
		AnagraficaRuolo ruolo = getAnagraficaRuolo(role);
		List<AnagraficaRuolo> res = new ArrayList<>();

		if (ruolo != null) {
			for (AbilitazioniRuolo ar : getAbilitazioniRuoli()) {
				for (Abilitazione a : ar.getAbilitazioni()) {
					if (a.getClass().equals(ModificaRuoloAbilitazione.class)) {
						ModificaRuoloAbilitazione ia = (ModificaRuoloAbilitazione) a;
						if (ia.getRuolo().equals(ruolo.getRuolo())) {
							AnagraficaRuolo r = getAnagraficaRuolo(ar.getRuolo());
							if (r != null && !res.contains(r)) {
								res.add(r);
							}
						}
					}
				}
			}
		}

		return res;
	}

	@Override
	public List<AnagraficaRuolo> getRuoliSuperutentiMatriceVisibilita(String role) {
		AnagraficaRuolo ruolo = getAnagraficaRuolo(role);
		List<AnagraficaRuolo> res = new ArrayList<>();

		if (ruolo != null) {
			for (AbilitazioniRuolo ar : getAbilitazioniRuoli()) {
				for (Abilitazione a : ar.getAbilitazioni()) {
					if (a.getClass().equals(VisibilitaRuoloAbilitazione.class)) {
						VisibilitaRuoloAbilitazione ia = (VisibilitaRuoloAbilitazione) a;
						if (ia.getRuolo().equals(ruolo.getRuolo())) {
							AnagraficaRuolo r = getAnagraficaRuolo(ar.getRuolo());
							if (r != null && !res.contains(r)) {
								res.add(r);
							}
						}
					}
				}
			}
		}

		return res;
	}

	@Override
	public List<AnagraficaRuolo> getRuoliSuperutentiMatriceVisibilita(TipologiaPratica tipoPratica) {

		List<AnagraficaRuolo> res = new ArrayList<>();

		if (tipoPratica != null) {
			for (AbilitazioniRuolo ar : getAbilitazioniRuoli()) {
				for (Abilitazione a : ar.getAbilitazioni()) {
					if (VisibilitaPraticaAbilitazione.class.isAssignableFrom(a.getClass())) {
						VisibilitaPraticaAbilitazione ia = (VisibilitaPraticaAbilitazione) a;
						if (ia.getTipo().equals(tipoPratica.getNomeTipologia())) {
							AnagraficaRuolo r = getAnagraficaRuolo(ar.getRuolo());
							if (r != null && !res.contains(r)) {
								res.add(r);
							}
						}
					}
				}
			}
		}

		return res;
	}

	@Override
	public List<String> controlloValiditaAbilitazioni(AbilitazioniRuolo abilitazioniRuolo) {

		List<String> errors = new ArrayList<>();

		for (Abilitazione a : abilitazioniRuolo.getAbilitazioni()) {

			if (a.getClass().equals(LetturaIngressoAbilitazione.class) && ((LetturaIngressoAbilitazione) a).isPrimoAssegnatario()) {
				LetturaIngressoAbilitazione lia = (LetturaIngressoAbilitazione) a;
				String ruolo = esistePrimoAssegnatario(lia.getTipo(), lia.getIndirizzo(), abilitazioniRuolo.getRuolo());
				if (ruolo != null) {
					errors.add("Esiste gia' un primo assegnatario per l'anagrafica " + lia.getIndirizzo() + " : " + ruolo);
					break;
				}

			} else if (a.getClass().equals(ModificaRuoloAbilitazione.class)
					&& isDipendenzaCircolare(abilitazioniRuolo.getRuolo(), ((ModificaRuoloAbilitazione) a).getRuolo(), ModificaRuoloAbilitazione.class)) {

				errors.add("Dipendenza circolare: il gruppo " + ((ModificaRuoloAbilitazione) a).getRuolo() + " e' supervisore di " + abilitazioniRuolo.getRuolo());
				break;
			}
		}

		return errors;
	}

	private String esistePrimoAssegnatario(String nomeTipologia, String indirizzo, String ruolo) {

		for (AbilitazioniRuolo abr : getAbilitazioniRuoli()) {
			if ((ruolo != null && !abr.getRuolo().equals(ruolo)) || ruolo == null) {
				for (Abilitazione a : abr.getAbilitazioni()) {
					if (a.getClass().equals(LetturaIngressoAbilitazione.class)) {
						LetturaIngressoAbilitazione via = (LetturaIngressoAbilitazione) a;
						if (via.getTipo().equals(nomeTipologia) && via.getIndirizzo().equalsIgnoreCase(indirizzo) && via.isPrimoAssegnatario()) {
							return abr.getRuolo();
						}
					}
				}
			}
		}

		return null;
	}

	private boolean isDipendenzaCircolare(String ruoloSupervisore, String ruoloSubordinato, Class<? extends RuoloAbilitazione> clazz) {

		for (AbilitazioniRuolo abr : getAbilitazioniRuoli()) {
			if (abr.getRuolo().equals(ruoloSubordinato)) {
				for (Abilitazione a : abr.getAbilitazioni()) {
					if (a.getClass().equals(clazz)) {
						RuoloAbilitazione via = (RuoloAbilitazione) a;
						if (via.getRuolo().equals(ruoloSupervisore)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	@Value("#{portlet['adminUsername']}")
	public void setAdminUsername(String username) {
		this.adminUsername = username;
	}

	@Value("#{portlet['adminNome']}")
	public void setAdminNome(String nome) {
		this.adminNome = nome;
	}

	@Value("#{portlet['adminCognome']}")
	public void setAdminCognome(String cognome) {
		this.adminCognome = cognome;
	}

	@Value("#{portlet['adminRuolo']}")
	public void setAdminRuolo(String ruolo) {
		this.adminRuolo = ruolo;
	}

	@Value("#{portlet['adminMatricola']}")
	public void setAdminMatricola(String matricola) {
		this.adminMatricola = matricola;
	}

	@Value("#{portlet['adminCodiceFiscale']}")
	public void setAdminCodiceFiscale(String codiceFiscale) {
		this.adminCodiceFiscale = codiceFiscale;
	}

	private void caricaAnagrafichePratiche(Utente utente) {
		logger.info("Inizio caricamento anagrafiche pratiche");

		lock.writeLock().lock();

		try {
			this.anagraficheFascicoli.clear();
			this.anagraficheFascicoli = configurazioniClient.getAnagraficheFascicoli(utente);
			this.anagraficheFascicoli.removeAll(Collections.singleton(null));

			this.anagraficheComunicazione.clear();
			this.anagraficheComunicazione = configurazioniClient.getAnagraficheComunicazioni(utente);
			this.anagraficheComunicazione.removeAll(Collections.singleton(null));

			this.anagraficheIngressi.clear();
			this.anagraficheIngressi = configurazioniClient.getAnagraficheIngressi(utente);
			this.anagraficheIngressi.removeAll(Collections.singleton(null));

			this.anagraficheMailInUscita.clear();
			this.anagraficheMailInUscita = configurazioniClient.getAnagraficheMailInUscita(utente);
			this.anagraficheMailInUscita.removeAll(Collections.singleton(null));

			this.anagraficheModelli.clear();
			this.anagraficheModelli = configurazioniClient.getAnagraficheModelli(utente);
			this.anagraficheModelli.removeAll(Collections.singleton(null));

			this.anagrafichePraticaModulistica.clear();
			this.anagrafichePraticaModulistica = configurazioniClient.getAnagrafichePraticaModulistica(utente);
			this.anagrafichePraticaModulistica.removeAll(Collections.singleton(null));

		} finally {
			lock.writeLock().unlock();
			logger.info("Fine caricamento anagrafiche pratiche");
		}

	}

	private void caricaAbilitazioniRuoli(Utente utente) {

		logger.info("Inizio caricamento abilitazioni ruoli");
		lock.writeLock().lock();

		try {
			this.abilitazioniRuoli.clear();
			this.abilitazioniRuoli = configurazioniClient.getAbilitazioniRuoli(utente);
			this.abilitazioniRuoli.removeAll(Collections.singleton(null));

		} finally {
			logger.info("Fine caricamento abilitazioni ruoli");
			lock.writeLock().unlock();
		}

	}

	private void caricaAnagraficheRuoli(Utente utente) {
		logger.info("Inizio caricamento anagrafiche ruoli");

		lock.writeLock().lock();

		try {
			this.anagraficheRuoli.clear();
			this.anagraficheRuoli = configurazioniClient.getAnagraficheRuoli(utente);
			this.anagraficheRuoli.removeAll(Collections.singleton(null));

			this.ruoliPersonali.clear();
			this.ruoliPersonali = configurazioniClient.getRuoliPersonali(utente);
			this.ruoliPersonali.removeAll(Collections.singleton(null));

			if (!this.ruoliPersonali.contains(utente.getRuoloPersonale())) {
				this.ruoliPersonali.add(utente.getRuoloPersonale());
			}

		} finally {
			lock.writeLock().unlock();
			logger.info("Fine caricamento anagrafiche ruoli");
		}

	}

	private void caricaSettori(Utente utente) {

		logger.info("Inizio caricamento settori");
		lock.writeLock().lock();

		try {
			this.settori.clear();
			this.settori = configurazioniClient.getSettori(utente);
			this.settori.removeAll(Collections.singleton(null));

		} finally {
			logger.info("Fine caricamento settori");
			lock.writeLock().unlock();
		}
	}

	private void caricaProprietaGenerali(Utente utente) {
		logger.info("Inizio caricamento proprietà generali");
		lock.writeLock().lock();

		try {
			this.proprietaGenerali = configurazioniClient.getProprietaGenerali(utente);

		} finally {
			lock.writeLock().unlock();
			logger.info("Fine caricamento proprietà generali");
		}

	}

	@Override
	public void reloadSettori(Utente utenteSpagic) {
		this.caricaSettori(utenteSpagic);
	}
}
