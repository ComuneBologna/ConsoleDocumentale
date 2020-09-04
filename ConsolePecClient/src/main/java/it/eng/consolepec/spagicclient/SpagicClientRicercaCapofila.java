package it.eng.consolepec.spagicclient;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.CapofilaDetail;

public interface SpagicClientRicercaCapofila {

	/**
	 * 
	 * Cerca un capofila all'interno di BA01
	 * 
	 * @param numeroPg
	 * @param annoPg
	 * @param utente
	 * @return
	 * @throws SpagicClientException
	 */
	public CapofilaDetail cerca(String numeroPg, int annoPg, Utente utente) throws SpagicClientException;

}
