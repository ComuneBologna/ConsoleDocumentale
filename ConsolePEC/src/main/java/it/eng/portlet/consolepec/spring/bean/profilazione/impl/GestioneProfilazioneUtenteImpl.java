package it.eng.portlet.consolepec.spring.bean.profilazione.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaWorklist;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaWorklist.Counter;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaWorklist.Stato;
import it.eng.cobo.consolepec.commons.configurazioni.Operazione;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.ModificaComunicazioneAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.ModificaEmailOutAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.ModificaFascicoloAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.ModificaIngressoAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.ModificaModelloAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.ModificaPraticaModulisticaAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.ModificaRuoloAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.RuoloAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.WorklistAbilitazione;
import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente;
import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente.PreferenzeFirmaDigitale;
import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente.PreferenzeRiassegnazione;
import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.cobo.consolepec.commons.exception.ConsoleDocumentaleException;
import it.eng.cobo.consolepec.commons.ldap.LdapQueryFilter;
import it.eng.cobo.consolepec.commons.ldap.LdapUser;
import it.eng.cobo.consolepec.commons.pratica.OperativitaRidotta;
import it.eng.cobo.consolepec.commons.pratica.OperativitaRidotta.OperazioneOperativitaRidotta;
import it.eng.cobo.consolepec.commons.pratica.OperativitaRidotta.OperazioneOperativitaRidotta.TipoAccesso;
import it.eng.cobo.consolepec.commons.profilazione.Utente;
import it.eng.cobo.consolepec.security.handler.AutorizzazioneHandler;
import it.eng.cobo.consolepec.security.handler.AutorizzazioneHandler.CondizioneAbilitazione;
import it.eng.cobo.consolepec.security.handler.AutorizzazioneHandler.QueryAbilitazione;
import it.eng.cobo.consolepec.util.json.JsonFactory;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.consolepec.client.ProfilazioneUtenteClient;
import it.eng.consolepec.xmlplugin.factory.ITipoApiTask;
import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Collegamento;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.portlet.consolepec.gwt.server.rest.RestClientInvoker;
import it.eng.portlet.consolepec.gwt.server.rest.RestResponse;
import it.eng.portlet.consolepec.spring.bean.configurazioni.GestioneConfigurazioni;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;
import it.eng.portlet.consolepec.spring.bean.session.user.ConsolePecUser;
import it.eng.portlet.consolepec.spring.bean.session.user.UserCustomFields;
import it.eng.portlet.consolepec.spring.bean.session.user.UserException;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

/**
 *
 * Il bean viene invocato attraverso il suo init-method all'avvio di ogni sessione utente caricando tutte le informazioni / abilitazioni / preferenze dell'utente
 *
 * @author biagiot
 *
 */
public class GestioneProfilazioneUtenteImpl implements GestioneProfilazioneUtente {

	private static final Logger logger = LoggerFactory.getLogger(GestioneProfilazioneUtenteImpl.class);
	private static final String SEPARATORE = "|";
	private static final String LIFERAY_CUSTOMFIELD_PASSWORDFIRMA_NAME = "consolepecpasswordfirma";
	private static final String LIFERAY_CUSTOMFIELD_USERNAMEFIRMA_NAME = "consolepecuserfirma";
	private static final String LIFERAY_CUSTOMFIELD_SETTORERIASSEGNA_NAME = "consolepecsettoreriassegna";
	private static final String LIFERAY_CUSTOMFIELD_GRUPPORIASSEGNA_NAME = "consolepecgrupporiassegna";
	private static final String LIFERAY_CUSTOMFIELD_INDIRIZZIRIASSEGNA_NAME = "consolepecindirizziriassegna";
	private static final String LIFERAY_CUSTOMFIELD_RICORDARIASSEGNA_NAME = "consolepecricordariassegna";

	@Autowired
	ProfilazioneUtenteClient profilazioneUtenteClient;

	@Autowired
	UserSessionUtil userSessionUtil;

	@Autowired
	UserCustomFields userCustomFields;

	@Autowired
	StandardPBEStringEncryptor encryptor;

	@Autowired
	GestioneConfigurazioni gestioneConfigurazioni;

	@Autowired
	RestClientInvoker restClientInvoker;

	private Utente datiUtente;
	private PreferenzeUtente preferenzeUtente;
	private AutorizzazioneHandler autorizzazioneHandler;
	private PreferenzeFirmaDigitale preferenzeFirmaDigitale;
	private PreferenzeRiassegnazione preferenzeRiassegnazione;
	private Map<AnagraficaWorklist, Counter> worklistAbilitate = new LinkedHashMap<AnagraficaWorklist, Counter>();

	public void init() {
		gestioneConfigurazioni.reload(userSessionUtil.getUtenteSpagic());

		try {
			restClientInvoker.login();

		} catch (ConsoleDocumentaleException e) {
			logger.error("Errore durante il login", e);
			throw new RuntimeException("Errore durante l'autenticazione", e);
		}

		try {
			caricaAnagraficheRuoliUtente();
			caricaAutorizzazioniUtente();
			caricaPreferenze();
			getWorklistAbilitate(true);

		} catch (ConsoleDocumentaleException e) {
			logger.error("Errore durante il recupero dei dati dell'utente {}", userSessionUtil.getUtenteSpagic().getUsername(), e);
			throw new RuntimeException("Errore durante il recupero dei dati dell'utente " + userSessionUtil.getUtenteSpagic().getUsername(), e);
		}

	}

	private void caricaAnagraficheRuoliUtente() throws ConsoleDocumentaleException {
		logger.info("Inizo caricamento ruoli utente {}", userSessionUtil.getUtenteSpagic().getUsername());
		gestioneConfigurazioni.addRuoloPersonale(userSessionUtil.getUtenteSpagic());

		List<AnagraficaRuolo> roles = profilazioneUtenteClient.getAnagraficheRuoliUtente(userSessionUtil.getUtenteSpagic());
		roles = Lists.newArrayList(Iterables.filter(roles, new Predicate<AnagraficaRuolo>() {

			@Override
			public boolean apply(AnagraficaRuolo input) {
				return it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo.Stato.ATTIVA.equals(input.getStato());
			}
		}));

		for (AnagraficaRuolo ar : roles) {
			userSessionUtil.getRuoli().add(ar.getRuolo());
		}

		LdapQueryFilter ricerca = new LdapQueryFilter();
		ricerca.setUtente(userSessionUtil.getUtenteSpagic().getUsername());
		HttpEntity entity = new StringEntity(JsonFactory.defaultFactory().serialize(ricerca), ContentType.APPLICATION_JSON);

		RestResponse output = null;

		try {
			output = restClientInvoker.customPost("/service/ldap/utenti", null, entity);

		} catch (ConsoleDocumentaleException e) {
			logger.error("Errore durante l'invocazione del servizio: " + "/service/ldap/utenti");
			throw e;
		}

		if (!output.isOk()) {
			throw new ApplicationException("Errore nella chiamata al servizio: " + "/service/ldap/utenti - " + output.getJson(), false);
		}

		String json = output.getJson();
		List<LdapUser> utentiLdap = JsonFactory.defaultFactory().deserializeList(json, LdapUser.class);

		String dipartimento = null;
		if (utentiLdap == null || utentiLdap.isEmpty() || utentiLdap.size() > 1) {
			logger.warn("Utente con username " + userSessionUtil.getUtenteSpagic().getUsername() + " non trovato o non univoco su LDAP");

		} else {
			dipartimento = utentiLdap.get(0).getDipartimento();

		}

		boolean ruoloEsterno = false;
		for (AnagraficaRuolo ar : roles) {
			if (ar.isEsterno()) {
				ruoloEsterno = true;
				break;
			}
		}

		this.datiUtente = Utente.builder().nome(userSessionUtil.getUtenteSpagic().getNome()).cognome(userSessionUtil.getUtenteSpagic().getCognome()).username(
				userSessionUtil.getUtenteSpagic().getUsername()).nomeCompleto(userSessionUtil.getUtenteSpagic().getFullName()).matricola(
						userSessionUtil.getUtenteSpagic().getMatricola()).codicefiscale(userSessionUtil.getUtenteSpagic().getCodicefiscale()).ruoloPersonale(
								userSessionUtil.getUtenteSpagic().getRuoloPersonale()).dipartimento(dipartimento).utenteEsterno(
										userSessionUtil.getRuoli().contains("ExternalUserGroup") || ruoloEsterno).anagraficheRuoli(
										roles).build();

		logger.info("Fine caricamento ruoli utente {}", userSessionUtil.getUtenteSpagic().getUsername());
	}

	private void caricaAutorizzazioniUtente() {
		logger.info("Inizo caricamento autorizzazioni utente {}", userSessionUtil.getUtenteSpagic().getUsername());
		autorizzazioneHandler = profilazioneUtenteClient.getAutorizzazioniUtente(userSessionUtil.getUtenteSpagic());
		logger.info("Fine caricamento autorizzazioni utente {}", userSessionUtil.getUtenteSpagic().getUsername());
	}

	private void caricaPreferenze() {
		caricaPreferenzeUtente();
		caricaPreferenzeFirmaDigitale();
		caricaPreferenzeRiassegnazione();
	}

	private void caricaPreferenzeUtente() {
		logger.info("Inizo caricamento preferenze utente {}", userSessionUtil.getUtenteSpagic().getUsername());
		preferenzeUtente = profilazioneUtenteClient.getPreferenzeUtente(userSessionUtil.getUtenteSpagic());
		logger.info("Fine caricamento preferenze utente {}", userSessionUtil.getUtenteSpagic().getUsername());
	}

	private void caricaPreferenzeFirmaDigitale() {
		logger.info("Inizo caricamento preferenze firma digitale {}", userSessionUtil.getUtenteSpagic().getUsername());
		PreferenzeFirmaDigitale preferenzeFirmaDigitale = new PreferenzeFirmaDigitale();

		String username = (String) userSessionUtil.getUtenteConsolePEC().getCustomAttribute(LIFERAY_CUSTOMFIELD_USERNAMEFIRMA_NAME);
		if (username != null && !username.trim().isEmpty()) {
			username = decrypt(username);
		}

		String password = (String) userSessionUtil.getUtenteConsolePEC().getCustomAttribute(LIFERAY_CUSTOMFIELD_PASSWORDFIRMA_NAME);
		if (password != null && !password.trim().isEmpty()) {
			password = decrypt(password);
		}

		preferenzeFirmaDigitale.setUsername(username);
		preferenzeFirmaDigitale.setPassword(password);
		this.preferenzeFirmaDigitale = preferenzeFirmaDigitale;

		logger.info("Fine caricamento preferenze firma digitale {}", userSessionUtil.getUtenteSpagic().getUsername());
	}

	private void caricaPreferenzeRiassegnazione() {
		logger.info("Inizo caricamento preferenze riassegnazione {}", userSessionUtil.getUtenteSpagic().getUsername());

		PreferenzeRiassegnazione preferenzeRiassegnazione = new PreferenzeRiassegnazione();
		String settore = (String) userSessionUtil.getUtenteConsolePEC().getCustomAttribute(LIFERAY_CUSTOMFIELD_SETTORERIASSEGNA_NAME);
		preferenzeRiassegnazione.setSettore(settore);
		String ruolo = (String) userSessionUtil.getUtenteConsolePEC().getCustomAttribute(LIFERAY_CUSTOMFIELD_GRUPPORIASSEGNA_NAME);
		preferenzeRiassegnazione.setRuolo(ruolo);
		Object indirizziEmail = userSessionUtil.getUtenteConsolePEC().getCustomAttribute(LIFERAY_CUSTOMFIELD_INDIRIZZIRIASSEGNA_NAME);
		if (indirizziEmail != null) {
			preferenzeRiassegnazione.getIndirizziNotifica().addAll(new ArrayList<>(Arrays.asList(StringUtils.split((String) indirizziEmail, SEPARATORE))));
		}
		Object ricordaScelta = userSessionUtil.getUtenteConsolePEC().getCustomAttribute(LIFERAY_CUSTOMFIELD_RICORDARIASSEGNA_NAME);
		if (ricordaScelta != null) {
			preferenzeRiassegnazione.setRicordaScelta(Boolean.valueOf((String) ricordaScelta));
		} else {
			preferenzeRiassegnazione.setRicordaScelta(false);
		}

		this.preferenzeRiassegnazione = preferenzeRiassegnazione;
		logger.info("Fine caricamento preferenze riassegnazione {}", userSessionUtil.getUtenteSpagic().getUsername());
	}

	@Override
	public PreferenzeUtente getPreferenzeUtente() {
		return preferenzeUtente;
	}

	@Override
	public AutorizzazioneHandler getAutorizzazioniUtente() {
		return autorizzazioneHandler;
	}

	@Override
	public Utente getDatiUtente() {
		return datiUtente;
	}

	@Override
	public PreferenzeFirmaDigitale getPreferenzeFirmaDigitale() {
		return preferenzeFirmaDigitale;
	}

	@Override
	public PreferenzeRiassegnazione getPreferenzeRiassegnazione() {
		return preferenzeRiassegnazione;
	}

	@Override
	public void aggiornaPreferenzeUtente(PreferenzeUtente preferenzeUtente) {
		logger.info("Inizio aggiornamento preferenze utente {}", userSessionUtil.getUtenteSpagic().getUsername());
		this.preferenzeUtente = profilazioneUtenteClient.aggiornaPreferenzeUtente(preferenzeUtente, userSessionUtil.getUtenteSpagic());
		logger.info("Fine aggiornamento preferenze utente {}", userSessionUtil.getUtenteSpagic().getUsername());
	}

	@Override
	public void aggiornaPreferenzeRiassegnazione(PreferenzeRiassegnazione preferenzeRiassegnazione) throws UserException {
		logger.info("Inizio aggiornamento preferenze riassegnazione {}", userSessionUtil.getUtenteSpagic().getUsername());

		userCustomFields.updateCustomField(userSessionUtil.getUtenteConsolePEC(), LIFERAY_CUSTOMFIELD_SETTORERIASSEGNA_NAME, preferenzeRiassegnazione.getSettore());
		userCustomFields.updateCustomField(userSessionUtil.getUtenteConsolePEC(), LIFERAY_CUSTOMFIELD_GRUPPORIASSEGNA_NAME, preferenzeRiassegnazione.getRuolo());
		userCustomFields.updateCustomField(userSessionUtil.getUtenteConsolePEC(), LIFERAY_CUSTOMFIELD_INDIRIZZIRIASSEGNA_NAME,
				StringUtils.join(preferenzeRiassegnazione.getIndirizziNotifica(), SEPARATORE));
		userCustomFields.updateCustomField(userSessionUtil.getUtenteConsolePEC(), LIFERAY_CUSTOMFIELD_RICORDARIASSEGNA_NAME, String.valueOf(preferenzeRiassegnazione.isRicordaScelta()));
		this.preferenzeRiassegnazione = preferenzeRiassegnazione;

		logger.info("Fine aggiornamento preferenze riassegnazione {}", userSessionUtil.getUtenteSpagic().getUsername());
	}

	@Override
	public void aggiornaPreferenzeFirmaDigitale(PreferenzeFirmaDigitale preferenzeFirmaDigitale) throws UserException {
		logger.info("Inizio aggiornamento preferenze firma digitale {}", userSessionUtil.getUtenteSpagic().getUsername());

		ConsolePecUser utenteConsolePEC = userSessionUtil.getUtenteConsolePEC();
		String password = encrypt(preferenzeFirmaDigitale.getPassword());
		String username = encrypt(preferenzeFirmaDigitale.getUsername());

		if (password != null && !password.isEmpty()) {
			userCustomFields.updateCustomField(utenteConsolePEC, LIFERAY_CUSTOMFIELD_PASSWORDFIRMA_NAME, password);
		}

		if (username != null && !username.isEmpty()) {
			userCustomFields.updateCustomField(utenteConsolePEC, LIFERAY_CUSTOMFIELD_USERNAMEFIRMA_NAME, username);
		}

		this.preferenzeFirmaDigitale = preferenzeFirmaDigitale;

		logger.info("Fine aggiornamento preferenze firma digitale {}", userSessionUtil.getUtenteSpagic().getUsername());
	}

	@Override
	public void eliminaPreferenzeRiassegnazione() throws UserException {
		logger.info("Inizio eliminazione preferenze riassegnazione {}", userSessionUtil.getUtenteSpagic().getUsername());

		userCustomFields.deleteCustomField(userSessionUtil.getUtenteConsolePEC(), LIFERAY_CUSTOMFIELD_SETTORERIASSEGNA_NAME);
		userCustomFields.deleteCustomField(userSessionUtil.getUtenteConsolePEC(), LIFERAY_CUSTOMFIELD_GRUPPORIASSEGNA_NAME);
		userCustomFields.deleteCustomField(userSessionUtil.getUtenteConsolePEC(), LIFERAY_CUSTOMFIELD_INDIRIZZIRIASSEGNA_NAME);
		userCustomFields.deleteCustomField(userSessionUtil.getUtenteConsolePEC(), LIFERAY_CUSTOMFIELD_RICORDARIASSEGNA_NAME);

		logger.info("Inizio eliminazione preferenze riassegnazione {}", userSessionUtil.getUtenteSpagic().getUsername());
	}

	@Override
	public void eliminaPreferenzeFirmaDigitale() throws UserException {
		logger.info("Inizio eliminazione preferenze firma digitale {}", userSessionUtil.getUtenteSpagic().getUsername());

		userCustomFields.deleteCustomField(userSessionUtil.getUtenteConsolePEC(), LIFERAY_CUSTOMFIELD_PASSWORDFIRMA_NAME);
		userCustomFields.deleteCustomField(userSessionUtil.getUtenteConsolePEC(), LIFERAY_CUSTOMFIELD_USERNAMEFIRMA_NAME);

		logger.info("Fine eliminazione preferenze firma digitale {}", userSessionUtil.getUtenteSpagic().getUsername());
	}

	private String encrypt(String input) {
		try {
			return encryptor.encrypt(input);
		} catch (Exception e) {
			logger.error("Errore in fase di cifratura", e);
			return null;
		}
	}

	private String decrypt(String input) {
		try {
			return encryptor.decrypt(input);
		} catch (Exception e) {
			logger.error("Errore in fase di decifratura", e);
			return null;
		}
	}

	@Override
	public <T extends RuoloAbilitazione> boolean isSuperutenteAbilitato(final String ruoloSubordinato, final Class<T> clazz) {
		QueryAbilitazione<T> qab = new QueryAbilitazione<T>();
		qab.addCondition(new CondizioneAbilitazione<T>() {

			@Override
			protected boolean valutaCondizione(T abilitazione) {
				return abilitazione.getRuolo().equals(ruoloSubordinato);
			}
		});

		return autorizzazioneHandler.isAbilitato(clazz, qab);
	}

	@Override
	public List<Operazione> getOperazioniAbilitate(final TipologiaPratica tipologiaPratica) {
		return new ArrayList<Operazione>();
	}

	@Override
	public Map<AnagraficaWorklist, Counter> getWorklistAbilitate(boolean reload) {
		logger.info("Inizio caricamento worklist utente {}", userSessionUtil.getUtenteSpagic().getUsername());

		if (reload || worklistAbilitate.isEmpty()) {
			worklistAbilitate.clear();

			List<WorklistAbilitazione> abilitazioni = autorizzazioneHandler.findAbilitazioni(WorklistAbilitazione.class);

			if (abilitazioni.size() > 1) {
				Collections.sort(abilitazioni, new Comparator<WorklistAbilitazione>() {

					@Override
					public int compare(WorklistAbilitazione o1, WorklistAbilitazione o2) {

						int i1 = o1.getPosizione() - o2.getPosizione();

						if (i1 == 0) {
							return o1.getNome().compareToIgnoreCase(o2.getNome());
						}

						return i1;
					}
				});
			}

			Map<AnagraficaWorklist, Counter> worklist = profilazioneUtenteClient.caricaWorklist(userSessionUtil.getUtenteSpagic(), reload);

			for (WorklistAbilitazione a : abilitazioni) {
				for (Entry<AnagraficaWorklist, Counter> entry : worklist.entrySet()) {
					if (entry.getKey().getNome().equals(a.getNome()) && entry.getKey().getStato().equals(Stato.ATTIVA)) {
						if (!reload) {
							if (worklistAbilitate.get(entry.getKey()) != null) {
								worklistAbilitate.put(entry.getKey(), worklistAbilitate.get(entry.getKey()));
							}

						} else {
							worklistAbilitate.put(entry.getKey(), entry.getValue());
						}
					}
				}
			}
		}

		logger.info("Fine caricamento worklist utente {}", userSessionUtil.getUtenteSpagic().getUsername());
		return worklistAbilitate;
	}

	@Override
	public boolean isOperazioneAbilitata(TipologiaPratica tipologiaPratica, Task<?> task, ITipoApiTask operazione,
			List<it.eng.consolepec.xmlplugin.factory.DatiPratica.Operazione> operazioniSuperutenti, List<it.eng.consolepec.xmlplugin.factory.DatiPratica.Operazione> operazioniAssegnazioneEsterna,
			List<Collegamento> collegamenti, OperativitaRidotta operativitaRidotta, String indirizzoEmail) {
		return isOperazioneAbilitata(tipologiaPratica, task, operazione, operazioniSuperutenti, operazioniAssegnazioneEsterna, collegamenti, operativitaRidotta, indirizzoEmail, false);
	}

	@Override
	public boolean isOperazioneAbilitata(TipologiaPratica tipologiaPratica, Task<?> task, ITipoApiTask operazione,
			List<it.eng.consolepec.xmlplugin.factory.DatiPratica.Operazione> operazioniSuperutenti, List<it.eng.consolepec.xmlplugin.factory.DatiPratica.Operazione> operazioniAssegnazioneEsterna,
			List<Collegamento> collegamenti, OperativitaRidotta operativitaRidotta, String indirizzoEmail, boolean checkAbilitazioneLettura) {

		AnagraficaRuolo anagraficaRuoloAssegnatario = gestioneConfigurazioni.getAnagraficaRuolo(task.getDati().getAssegnatario().getNome());
		boolean isAssegnatario = datiUtente.getAnagraficheRuoli().contains(anagraficaRuoloAssegnatario);

		/*
		 * Se l'operazione è esclusiva del superutente e l'utente corrente non è il superutente
		 */
		if (!isSuperutenteAbilitato(task.getDati().getAssegnatario().getNome(), ModificaRuoloAbilitazione.class) && isOperazioneEsclusivaSuperutente(operazione, operazioniSuperutenti)) {
			return false;
		}

		// - check operazione abilitata
		// 1- utente è assegnatario
		// 2- l'utente corrente è il superutente dell'assegnatario ed è abilitato per l'operazione
		// 3- abilit. per collegamento
		// 4- abilt. per utente esterno

		if (!isOperazioneAbilitata(task, operazione) && !isModificaAbilitata(tipologiaPratica, indirizzoEmail, operazione)) {
			return false;

		} else if (isAssegnatario && checkOperativitaRidotta(operativitaRidotta, operazione, TipoAccesso.ASSEGNATARIO)) {
			return true;

		} else if (isOperazioneSuperutenteAbilitata(anagraficaRuoloAssegnatario.getRuolo(), operazione, operazioniSuperutenti, tipologiaPratica, indirizzoEmail)
				&& checkOperativitaRidotta(operativitaRidotta, operazione, TipoAccesso.SUPERVISORE)) {
			return true;

		} else if (checkAbilitazioneCollegamento(tipologiaPratica, operazione, collegamenti) && checkOperativitaRidotta(operativitaRidotta, operazione, TipoAccesso.COLLEGAMENTO)) {
			return true;

		} else if (checkAbilitazioneUtenteEsterno(operazione, operazioniAssegnazioneEsterna) && checkOperativitaRidotta(operativitaRidotta, operazione, TipoAccesso.UTENTE_ESTERNO)) {
			return true;
		}

		if (checkAbilitazioneLettura) {
			return checkOperazioneVisibilita(tipologiaPratica, anagraficaRuoloAssegnatario, operazione, operativitaRidotta);
		}

		return false;
	}

	@Override
	public boolean checkOperazioneVisibilita(TipologiaPratica tipologiaPratica, AnagraficaRuolo assegnatario, ITipoApiTask operazione, OperativitaRidotta operativitaRidotta) {

		if (hasMatriceVisibilita(tipologiaPratica, assegnatario) && checkOperativitaRidotta(operativitaRidotta, operazione, TipoAccesso.MATR_VISIBILITA)) {
			return true;
		}

		if (checkOperativitaRidotta(operativitaRidotta, operazione, TipoAccesso.LETTURA)) {
			return true;
		}

		return false;
	}

	private boolean hasMatriceVisibilita(TipologiaPratica tipologiaPratica, AnagraficaRuolo assegnatario) {
		return !Collections.disjoint(datiUtente.getAnagraficheRuoli(), gestioneConfigurazioni.getRuoliSuperutentiMatriceVisibilita(assegnatario.getRuolo()))

				|| !Collections.disjoint(datiUtente.getAnagraficheRuoli(), gestioneConfigurazioni.getRuoliSuperutentiMatriceVisibilita(tipologiaPratica));

	}

	private static boolean checkOperativitaRidotta(OperativitaRidotta operativitaRidotta, ITipoApiTask operazione, TipoAccesso tipoAccesso) {
		if (operativitaRidotta == null) {
			return true;
		}

		for (OperazioneOperativitaRidotta ovr : operativitaRidotta.getOperazioni()) {
			if (operazione.name().equals(ovr.getNomeOperazione())) {
				return ovr.getAccessiConsentiti().contains(tipoAccesso);
			}
		}

		return true;
	}

	private static boolean isOperazioneAbilitata(Task<?> task, ITipoApiTask tipoApiTask) {
		return task.controllaAbilitazione(tipoApiTask);
	}

	private static boolean checkOperazione(List<Operazione> operazioni, ITipoApiTask operazione) {
		for (Operazione o : operazioni) {
			if (operazione.name().equals(o.getNome())) {
				return true;
			}
		}

		return false;
	}

	private boolean isModificaAbilitata(final TipologiaPratica tipologiaPratica, final String indirizzoMail, final ITipoApiTask operazione) {

		if (PraticaUtil.isFascicolo(tipologiaPratica)) {
			QueryAbilitazione<ModificaFascicoloAbilitazione> qab = new QueryAbilitazione<ModificaFascicoloAbilitazione>();
			qab.addCondition(new CondizioneAbilitazione<ModificaFascicoloAbilitazione>() {

				@Override
				protected boolean valutaCondizione(ModificaFascicoloAbilitazione abilitazione) {
					return (TipologiaPratica.FASCICOLO.getNomeTipologia().equals(abilitazione.getTipo()) || abilitazione.getTipo().equals(tipologiaPratica.getNomeTipologia()))
							&& checkOperazione(abilitazione.getOperazioni(), operazione);
				}
			});

			return autorizzazioneHandler.isAbilitato(ModificaFascicoloAbilitazione.class, qab);

		} else if (PraticaUtil.isIngresso(tipologiaPratica)) {
			QueryAbilitazione<ModificaIngressoAbilitazione> qab = new QueryAbilitazione<ModificaIngressoAbilitazione>();
			qab.addCondition(new CondizioneAbilitazione<ModificaIngressoAbilitazione>() {

				@Override
				protected boolean valutaCondizione(ModificaIngressoAbilitazione abilitazione) {
					return abilitazione.getTipo().equals(tipologiaPratica.getNomeTipologia()) && abilitazione.getIndirizzo().equalsIgnoreCase(indirizzoMail)
							&& checkOperazione(abilitazione.getOperazioni(), operazione);
				}
			});

			return autorizzazioneHandler.isAbilitato(ModificaIngressoAbilitazione.class, qab);

		} else if (PraticaUtil.isEmailOut(tipologiaPratica)) {
			QueryAbilitazione<ModificaEmailOutAbilitazione> qab = new QueryAbilitazione<ModificaEmailOutAbilitazione>();
			qab.addCondition(new CondizioneAbilitazione<ModificaEmailOutAbilitazione>() {

				@Override
				protected boolean valutaCondizione(ModificaEmailOutAbilitazione abilitazione) {
					return abilitazione.getTipo().equals(tipologiaPratica.getNomeTipologia()) && abilitazione.getIndirizzo().equalsIgnoreCase(indirizzoMail)
							&& checkOperazione(abilitazione.getOperazioni(), operazione);
				}
			});

			return autorizzazioneHandler.isAbilitato(ModificaEmailOutAbilitazione.class, qab);

		} else if (PraticaUtil.isModello(tipologiaPratica)) {
			QueryAbilitazione<ModificaModelloAbilitazione> qab = new QueryAbilitazione<ModificaModelloAbilitazione>();
			qab.addCondition(new CondizioneAbilitazione<ModificaModelloAbilitazione>() {

				@Override
				protected boolean valutaCondizione(ModificaModelloAbilitazione abilitazione) {
					return abilitazione.getTipo().equals(tipologiaPratica.getNomeTipologia()) && checkOperazione(abilitazione.getOperazioni(), operazione);
				}
			});

			return autorizzazioneHandler.isAbilitato(ModificaModelloAbilitazione.class, qab);

		} else if (PraticaUtil.isPraticaModulistica(tipologiaPratica)) {
			QueryAbilitazione<ModificaPraticaModulisticaAbilitazione> qab = new QueryAbilitazione<ModificaPraticaModulisticaAbilitazione>();
			qab.addCondition(new CondizioneAbilitazione<ModificaPraticaModulisticaAbilitazione>() {

				@Override
				protected boolean valutaCondizione(ModificaPraticaModulisticaAbilitazione abilitazione) {
					return abilitazione.getTipo().equals(tipologiaPratica.getNomeTipologia()) && checkOperazione(abilitazione.getOperazioni(), operazione);
				}
			});

			return autorizzazioneHandler.isAbilitato(ModificaPraticaModulisticaAbilitazione.class, qab);

		} else if (PraticaUtil.isComunicazione(tipologiaPratica)) {
			QueryAbilitazione<ModificaComunicazioneAbilitazione> qab = new QueryAbilitazione<ModificaComunicazioneAbilitazione>();
			qab.addCondition(new CondizioneAbilitazione<ModificaComunicazioneAbilitazione>() {

				@Override
				protected boolean valutaCondizione(ModificaComunicazioneAbilitazione abilitazione) {
					return abilitazione.getTipo().equals(tipologiaPratica.getNomeTipologia()) && checkOperazione(abilitazione.getOperazioni(), operazione);
				}
			});

			return autorizzazioneHandler.isAbilitato(ModificaComunicazioneAbilitazione.class, qab);
		}

		return false;

	}

	@Override
	public boolean isOperazioneSuperutenteAbilitata(String ruoloAssegnatario, ITipoApiTask tipoApiTask, List<it.eng.consolepec.xmlplugin.factory.DatiPratica.Operazione> operazioniSuperutente,
			TipologiaPratica tipologiaPratica, String indirizzoEmail) {
		boolean isSuperutente = isSuperutenteAbilitato(ruoloAssegnatario, ModificaRuoloAbilitazione.class);
		boolean isAbilitato = false;

		for (it.eng.consolepec.xmlplugin.factory.DatiPratica.Operazione o : operazioniSuperutente) {
			if (tipoApiTask.name().equals(o.getNomeOperazione())) {
				isAbilitato = true;
				break;
			}
		}

		if (!isAbilitato) {
			isAbilitato = isModificaAbilitata(tipologiaPratica, indirizzoEmail, tipoApiTask);
		}

		return isSuperutente && isAbilitato;
	}

	private static boolean isOperazioneEsclusivaSuperutente(ITipoApiTask tipoApiTask, List<it.eng.consolepec.xmlplugin.factory.DatiPratica.Operazione> operazioniEsclusiveSuperutenti) {

		for (it.eng.consolepec.xmlplugin.factory.DatiPratica.Operazione o : operazioniEsclusiveSuperutenti) {
			if (tipoApiTask.name().equals(o.getNomeOperazione())) {
				return o.isAbilitata();
			}
		}

		return false;
	}

	private boolean checkAbilitazioneCollegamento(TipologiaPratica tipologiaPratica, ITipoApiTask operazione, List<Collegamento> collegamenti) {

		if (PraticaUtil.isFascicolo(tipologiaPratica)) {
			if (collegamenti == null || collegamenti.isEmpty()) {
				return false;
			}

			for (Collegamento collegamento : collegamenti) {

				if (userSessionUtil.getUtenteSpagic().getRuoli().contains(collegamento.getNomeGruppo())) {
					for (it.eng.consolepec.xmlplugin.factory.DatiPratica.Operazione o : collegamento.getOperazioniConsentite()) {
						if (o.getNomeOperazione().equals(operazione.name())) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	private boolean checkAbilitazioneUtenteEsterno(ITipoApiTask operazione, List<it.eng.consolepec.xmlplugin.factory.DatiPratica.Operazione> operazioniAssegnazioneEsterna) {

		if (datiUtente.isUtenteEsterno() && operazioniAssegnazioneEsterna != null) {

			if (operazione.equals(TipoApiTask.RITORNA_DA_INOLTRARE_ESTERNO)) {
				return true;
			}

			for (it.eng.consolepec.xmlplugin.factory.DatiPratica.Operazione o : operazioniAssegnazioneEsterna) {
				if (o.getNomeOperazione().equals(operazione.name())) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public boolean isRuoloUtenteAbilitato(AnagraficaRuolo ruoloAssegnatario) {
		if (userSessionUtil.getUtenteSpagic().getRuoli().contains(ruoloAssegnatario.getRuolo())) {
			return true;
		}

		return isSuperutenteAbilitato(ruoloAssegnatario.getRuolo(), ModificaRuoloAbilitazione.class);
	}

}
