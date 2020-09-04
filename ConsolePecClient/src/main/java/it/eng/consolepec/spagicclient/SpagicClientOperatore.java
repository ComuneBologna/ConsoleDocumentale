package it.eng.consolepec.spagicclient;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

public interface SpagicClientOperatore {

	public LockedPratica modifica(String pathPratica, String operatore, Utente utente) throws SpagicClientException;

}
