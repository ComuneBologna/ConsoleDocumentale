package it.eng.consolepec.client;

import it.eng.cobo.consolepec.commons.services.InputStreamMapper;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

/**
 * @author GiacomoFM
 * @since 13/mar/2019
 */
public interface UploadFileZipClient {

	LockedPratica uploadZip(String pathFascicolo, InputStreamMapper inputStreamMapper, Utente utente) throws SpagicClientException;

}
