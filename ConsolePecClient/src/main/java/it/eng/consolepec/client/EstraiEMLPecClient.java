package it.eng.consolepec.client;

import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

public interface EstraiEMLPecClient {

	LockedPratica estraiEMLPec(String idDocumentalePEC, String idDocumentalePratica, Utente utente) throws SpagicClientException;

}
