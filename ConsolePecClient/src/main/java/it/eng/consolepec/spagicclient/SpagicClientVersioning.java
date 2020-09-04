package it.eng.consolepec.spagicclient;

import it.bologna.comune.alfresco.versioning.AllVersions;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

public interface SpagicClientVersioning {

	/**
	 * 
	 * @param path
	 * @param nomeFile
	 * @return
	 * @throws SpagicClientException
	 */
	public AllVersions getAllVersions(String path, String nomeFile, Utente utente) throws SpagicClientException;

	/**
	 * 
	 * @param uuid
	 * @return
	 * @throws SpagicClientException
	 */
	public AllVersions getAllVersions(String uuid, Utente utente) throws SpagicClientException;
}
