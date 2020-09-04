package it.eng.consolepec.client;

import java.util.List;
import java.util.Map;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaIngresso;
import it.eng.cobo.consolepec.commons.configurazioni.services.AnagraficaIngressoResponse;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

/**
 *
 * @author biagiot
 *
 */
public interface AmministrazioneAnagraficaIngressoClient {

	List<AnagraficaIngresso> ricerca(Map<String, Object> filtri, Integer limit, Integer offset, String orderBy, Boolean sort, Utente utente) throws SpagicClientException;

	AnagraficaIngressoResponse crea(AnagraficaIngresso anagraficaIngresso, boolean creaEmailOut, Utente utente) throws SpagicClientException;

	AnagraficaIngressoResponse modifica(String indirizzoEmail, AnagraficaIngresso anagraficaIngresso, Utente utente) throws SpagicClientException;

	AnagraficaIngresso aggiornaPrimoAssegnatario(String tipologiaEmail, String indirizzoEmail, String nuovoAssegnatario, Utente utente) throws SpagicClientException;

	long count(Map<String, Object> filtri, Utente utente) throws SpagicClientException;

}
