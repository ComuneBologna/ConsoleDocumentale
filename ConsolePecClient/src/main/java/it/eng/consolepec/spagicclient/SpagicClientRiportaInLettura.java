package it.eng.consolepec.spagicclient;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

public interface SpagicClientRiportaInLettura {
	/**
	 * 
	 * @param pathPratica
	 * @param utente
	 * @return
	 * @throws SpagicClientException
	 */
	public LockedPratica riportaInLettura(String pathPratica, Utente utente) throws SpagicClientException;
}
