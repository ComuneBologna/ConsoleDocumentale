package it.eng.consolepec.client;

import java.util.List;

import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

public interface SpostaAllegatiClient {

	LockedPratica spostaAllegati(String idDocumentaleSorgente, String idDocumentaleDestinatario, List<String> nomiAllegati, Utente utente) throws SpagicClientException;
}
