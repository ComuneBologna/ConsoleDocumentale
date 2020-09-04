package it.eng.consolepec.spagicclient;

import java.io.InputStream;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

public interface SpagicClientDownlodAllegatoOriginale {

	/**
	 * 
	 * @param uuid
	 * @return
	 * @throws SpagicClientException
	 */
	public InputStream getDocumentByUuid(String uuid) throws SpagicClientException;

}
