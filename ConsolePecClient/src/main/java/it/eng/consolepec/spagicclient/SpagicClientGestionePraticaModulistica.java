package it.eng.consolepec.spagicclient;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

/**
 * @author Antonio
 * 
 */
public interface SpagicClientGestionePraticaModulistica {

	/**
	 * 
	 * @param pathPraticaModulistica
	 * @param username
	 * @return
	 * @throws SpagicClientException
	 */
	public boolean elimina(String pathPraticaModulistica, Utente utente) throws SpagicClientException;


}
