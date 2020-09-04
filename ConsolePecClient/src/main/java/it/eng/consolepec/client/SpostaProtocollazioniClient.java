package it.eng.consolepec.client;

import java.util.List;

import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

public interface SpostaProtocollazioniClient {

	public LockedPratica spostaProtocollazioni(String idDocumentaleSorgente, String idDocumentaleDestinatario, List<String> allegatiProtocollati, List<String> pathPraticheProtocollate,
			Utente utente) throws SpagicClientException;
}
