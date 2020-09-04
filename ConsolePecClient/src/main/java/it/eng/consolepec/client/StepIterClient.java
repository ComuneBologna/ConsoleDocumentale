package it.eng.consolepec.client;

import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

/**
 *
 * @author biagiot
 *
 */
public interface StepIterClient {

	public LockedPratica cambiaStepIter(String idDocumentale, String stepIter, Utente utente) throws SpagicClientException;

}
