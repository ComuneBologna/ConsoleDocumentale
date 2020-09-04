package it.eng.consolepec.client.impl;

import java.util.List;
import java.util.Map;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaWorklist;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaWorklist.Counter;
import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente;
import it.eng.cobo.consolepec.security.handler.AutorizzazioneHandler;
import it.eng.consolepec.client.AbstractConsolePecClient;
import it.eng.consolepec.client.ClientInvoker;
import it.eng.consolepec.client.ProfilazioneUtenteClient;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

/**
 *
 * @author biagiot
 *
 */
public class ProfilazioneUtenteClientImpl extends AbstractConsolePecClient implements ProfilazioneUtenteClient {

	public ProfilazioneUtenteClientImpl(ClientInvoker clientInvoker) {
		super(clientInvoker);
	}

	@Override
	public List<AnagraficaRuolo> getAnagraficheRuoliUtente(Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.PROFILAZIONE_UTENTE_CARICA_ANAGRAFICHE_RUOLI, utente);
	}

	@Override
	public PreferenzeUtente getPreferenzeUtente(Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.PROFILAZIONE_UTENTE_CARICA_PREFERENZE, utente);
	}

	@Override
	public PreferenzeUtente aggiornaPreferenzeUtente(PreferenzeUtente preferenzeUtente, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.PROFILAZIONE_UTENTE_AGGIORNA_PREFERENZE, utente, preferenzeUtente);
	}

	@Override
	public AutorizzazioneHandler getAutorizzazioniUtente(Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.PROFILAZIONE_UTENTE_CARICA_AUTORIZZAZIONI, utente);
	}

	@Override
	public Map<AnagraficaWorklist, Counter> caricaWorklist(Utente utente, boolean ricaricaContatori) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.PROFILAZIONE_UTENTE_CARICA_WORKLIST, utente, ricaricaContatori);
	}
}
