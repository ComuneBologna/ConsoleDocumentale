package it.eng.consolepec.client;

import java.util.List;
import java.util.Map;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

public interface AmministrazioneAnagraficaFascicoloClient {

	List<AnagraficaFascicolo> ricerca(Map<String, Object> filtri, Integer limit, Integer offset, String orderBy, Boolean sort, Utente utente) throws SpagicClientException;

	AnagraficaFascicolo crea(AnagraficaFascicolo anagraficaFascicolo, Utente utente) throws SpagicClientException;

	AnagraficaFascicolo modifica(AnagraficaFascicolo anagraficaFascicolo, Utente utente) throws SpagicClientException;

	long count(Map<String, Object> filtri, Utente utente) throws SpagicClientException;

}
