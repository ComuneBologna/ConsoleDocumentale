package it.eng.consolepec.client;

import java.util.List;

import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

public interface AssegnazioneEsternaClient {

	LockedPratica concludiAssegnazioneEsterna(String idDocumentale, Utente utente) throws SpagicClientException;

	LockedPratica assegna(String idDocumentale, List<String> destinatari, String testo, List<String> operazioni, Utente utente) throws SpagicClientException;

	LockedPratica modificaAssegnazioneEsterna(String idDocumentale, List<String> operazioni, Utente utente) throws SpagicClientException;
}
