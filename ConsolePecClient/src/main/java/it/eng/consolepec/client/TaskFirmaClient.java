package it.eng.consolepec.client;

import it.eng.cobo.consolepec.commons.atti.DocumentoTaskFirma;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

/**
 * @author GiacomoFM
 * @since 21/mar/2018
 */
public interface TaskFirmaClient {

	DocumentoTaskFirma getDocumentoTaskFirma(Utente utente, String idDocumentale, Integer idTaskFirma) throws SpagicClientException;

}
