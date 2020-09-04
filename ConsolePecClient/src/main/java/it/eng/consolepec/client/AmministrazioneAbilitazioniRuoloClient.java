package it.eng.consolepec.client;

import java.util.List;
import java.util.Map;

import it.eng.cobo.consolepec.commons.configurazioni.AbilitazioniRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.util.AbilitazioneRuoloSingola;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

public interface AmministrazioneAbilitazioniRuoloClient {

	List<AbilitazioniRuolo> carica(Utente utente) throws SpagicClientException;

	List<AbilitazioneRuoloSingola> aggregate(Map<String, Object> filters, Integer limit, Integer offset, String orderBy, Boolean sort, Utente utente) throws SpagicClientException;

	int countAggregate(Map<String, Object> filters, Utente utente) throws SpagicClientException;

}
