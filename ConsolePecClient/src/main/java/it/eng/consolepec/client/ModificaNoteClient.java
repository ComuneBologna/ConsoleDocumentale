package it.eng.consolepec.client;

import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

public interface ModificaNoteClient {

	public LockedPratica aggiungiNote(String idDocumentale, String note, Utente utente) throws SpagicClientException;

	public LockedPratica modificaNote(String idDocumentale, String note, Utente utente) throws SpagicClientException;

}
