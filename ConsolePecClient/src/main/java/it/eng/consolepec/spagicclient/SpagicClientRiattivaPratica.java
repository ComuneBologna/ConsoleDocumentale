package it.eng.consolepec.spagicclient;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

public interface SpagicClientRiattivaPratica {

	/**
	 * 
	 * @param pathPraticaDaRiattivare
	 * @param username
	 * @return
	 * @throws SpagicClientException
	 */
	public LockedPratica riattivaPratica(String pathPraticaDaRiattivare, Utente utente) throws SpagicClientException;
}
