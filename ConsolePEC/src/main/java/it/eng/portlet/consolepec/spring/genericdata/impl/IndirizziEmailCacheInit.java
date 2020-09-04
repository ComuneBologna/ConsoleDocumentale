package it.eng.portlet.consolepec.spring.genericdata.impl;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import it.eng.consolepec.spagicclient.Utente;
import it.eng.portlet.consolepec.spring.genericdata.IndirizzoEmailCachePrimoLivello;

public class IndirizziEmailCacheInit implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger logger = LoggerFactory.getLogger(IndirizziEmailCacheInit.class);

	private String adminUsername;
	private String adminNome;
	private String adminCognome;
	private String adminRuolo;
	private String adminMatricola;
	private String adminCodiceFiscale;

	@Autowired
	IndirizzoEmailCachePrimoLivello indirizzoEmailCachePrimoLivello;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		logger.info("Inizio caricamento cache indirizzi email");

		try {
			Utente consoleAdminUser = new Utente(adminNome, adminCognome, adminUsername, adminMatricola, adminCodiceFiscale, Arrays.asList(adminRuolo));
			indirizzoEmailCachePrimoLivello.getAllIndirizziEmail(consoleAdminUser);
			logger.info("Fine caricamento cache indirizzi email");

		} catch (Exception e) {
			logger.error("Errore durante il caricamento degli indirizzi email in cache", e);
		}

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
}
