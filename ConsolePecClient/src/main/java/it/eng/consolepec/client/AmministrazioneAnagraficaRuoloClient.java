package it.eng.consolepec.client;

import java.util.List;
import java.util.Map;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.Azione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.Abilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.services.AnagraficaRuoloResponse;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

public interface AmministrazioneAnagraficaRuoloClient {

	List<AnagraficaRuolo> ricerca(Map<String, Object> filtri, Integer limit, Integer offset, String orderBy, Boolean sort, Utente utente) throws SpagicClientException;

	long count(Map<String, Object> filtri, Utente utente) throws SpagicClientException;

	AnagraficaRuoloResponse crea(AnagraficaRuolo anagraficaRuolo, List<Abilitazione> abilitazioniRuolo, String settore, List<Azione> azioni, Utente utente) throws SpagicClientException;

	AnagraficaRuoloResponse modifica(AnagraficaRuolo anagraficaRuolo, List<Abilitazione> abilitazioniRuolo, String settore, List<Azione> azioni, Utente utente) throws SpagicClientException;
}
