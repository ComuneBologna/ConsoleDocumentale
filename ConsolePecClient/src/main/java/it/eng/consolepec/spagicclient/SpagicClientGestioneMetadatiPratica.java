package it.eng.consolepec.spagicclient;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

import java.util.Map;

/**
 *
 * @author biagiot
 *
 */
public interface SpagicClientGestioneMetadatiPratica {

	public Map<String, String> getEtichetteMetadatiMap(String tipoPratica, Utente utente) throws SpagicClientException;
}
