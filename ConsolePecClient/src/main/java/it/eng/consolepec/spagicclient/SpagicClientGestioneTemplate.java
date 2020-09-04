package it.eng.consolepec.spagicclient;

import java.util.Map;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

/**
 * @author Antonio
 *
 */
public interface SpagicClientGestioneTemplate {

	/**
	 *
	 * @param pathTemplate
	 * @param username
	 * @return
	 * @throws SpagicClientException
	 */
	public boolean elimina(String pathTemplate, Utente utente) throws SpagicClientException;

	/**
	 *
	 * @param pathFascicolo
	 * @param pathTemplate
	 * @param valori
	 * @param nomeFile
	 * @param utente
	 * @return
	 * @throws SpagicClientException
	 */
	public LockedPratica creaPDF(String pathFascicolo, String pathTemplate, Map<String, String> valori, String nomeFile, Utente utente) throws SpagicClientException;
}
