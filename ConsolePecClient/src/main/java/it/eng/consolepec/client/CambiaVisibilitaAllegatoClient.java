package it.eng.consolepec.client;

import java.util.List;

import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

public interface CambiaVisibilitaAllegatoClient {

	LockedPratica cambiaVisibilitaAllegato(String idDocumentale, String nomeAllegato, List<String> gruppi, Utente utente) throws SpagicClientException;
}
