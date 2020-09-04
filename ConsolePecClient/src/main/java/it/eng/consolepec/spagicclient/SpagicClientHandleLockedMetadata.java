package it.eng.consolepec.spagicclient;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

@Deprecated
public interface SpagicClientHandleLockedMetadata {

	/**
	 *
	 * @param pathAlfresco
	 * @param user
	 * @return
	 * @throws SpagicClientException
	 */
	@Deprecated
	public LockedPratica loadMetadatiXml(String pathAlfresco, Utente utente) throws SpagicClientException;

	/**
	 *
	 * @param praticaXml
	 * @param hash
	 * @param user
	 * @return
	 * @throws SpagicClientException
	 */
	@Deprecated
	public LockedPratica updateMetadatiXml(String praticaXml, String hash, Utente utente) throws SpagicClientException;
}
