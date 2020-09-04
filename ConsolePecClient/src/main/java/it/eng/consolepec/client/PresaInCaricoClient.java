package it.eng.consolepec.client;

import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

public interface PresaInCaricoClient {

	LockedPratica prendiInCarico(String idDocumentale, Utente utente) throws SpagicClientException;

	LockedPratica rilasciaInCarico(String idDocumentale, Utente utente) throws SpagicClientException;
}
