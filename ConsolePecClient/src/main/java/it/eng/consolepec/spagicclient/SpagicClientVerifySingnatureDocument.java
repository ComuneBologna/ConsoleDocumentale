package it.eng.consolepec.spagicclient;

import it.eng.cobo.consolepec.commons.firmadigitale.FirmaDigitale;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

public interface SpagicClientVerifySingnatureDocument {

	/**
	 * 
	 * @param uuid
	 * @return
	 * @throws SpagicClientException
	 */
	public FirmaDigitale verificaFirmaDocumentoByUuid(String uuid) throws SpagicClientException;

	/**
	 * 
	 * @param path
	 * @return
	 * @throws SpagicClientException
	 */
	public FirmaDigitale verificaFirmaDocumentoByPath(String path) throws SpagicClientException;

}
