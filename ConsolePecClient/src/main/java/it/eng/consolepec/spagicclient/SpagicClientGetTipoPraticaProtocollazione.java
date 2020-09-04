package it.eng.consolepec.spagicclient;

import it.bologna.comune.alfresco.protocollazione.Request;
import it.bologna.comune.alfresco.protocollazione.Response;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

public interface SpagicClientGetTipoPraticaProtocollazione {
	
	/**
	 * 
	 * @param request
	 * @return
	 * @throws SpagicClientException
	 */
	public Response getTipoPratica(Request request) throws SpagicClientException; 

}
