package it.eng.consolepec.spagicclient;

import it.bologna.comune.spagic.protocollazione.dettaglio.Response;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

public interface SpagicClientDettaglioPraticaProtocollozione {
	
	/**
	 * 
	 * @param identificativoPratica
	 * @return
	 * @throws SpagicClientException
	 */
	public Response getDettaglioPratica(String identificativoPratica) throws SpagicClientException;

}
