package it.eng.consolepec.client;

import java.util.List;
import java.util.Map;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaWorklist;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaWorklist.Counter;
import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente;
import it.eng.cobo.consolepec.security.handler.AutorizzazioneHandler;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

/**
 *
 * @author biagiot
 *
 */
public interface ProfilazioneUtenteClient {

	List<AnagraficaRuolo> getAnagraficheRuoliUtente(Utente utente) throws SpagicClientException;

	PreferenzeUtente getPreferenzeUtente(Utente utente) throws SpagicClientException;

	PreferenzeUtente aggiornaPreferenzeUtente(PreferenzeUtente preferenzeUtente, Utente utente) throws SpagicClientException;

	AutorizzazioneHandler getAutorizzazioniUtente(Utente utente) throws SpagicClientException;

	Map<AnagraficaWorklist, Counter> caricaWorklist(Utente utente, boolean ricaricaContatori) throws SpagicClientException;
}
