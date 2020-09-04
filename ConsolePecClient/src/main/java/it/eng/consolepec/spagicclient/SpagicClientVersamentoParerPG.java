package it.eng.consolepec.spagicclient;

import it.bologna.comune.spagic.versamentoparerpg.Response;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

public interface SpagicClientVersamentoParerPG {

	/**
	 * 
	 * comunica al sistema del protocollo il pg inviato al parer (inserimento)
	 * 
	 * @param numeroPg
	 * @param annoPg
	 * @param chiaveUD
	 * @param utente
	 * @return
	 * @throws SpagicClientException
	 */
	public Response inserimento(String numeroPg, int annoPg, String chiaveUD, Utente utente) throws SpagicClientException;
}
