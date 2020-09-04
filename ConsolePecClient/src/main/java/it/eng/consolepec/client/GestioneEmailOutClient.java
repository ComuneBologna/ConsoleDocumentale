package it.eng.consolepec.client;

import java.util.List;
import java.util.Map;

import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

public interface GestioneEmailOutClient {

	LockedPratica inviaBozza(String idDocumentaleBozza, Utente utente) throws SpagicClientException;

	LockedPratica eliminaBozza(String idDocumentaleBozza, String idDocumentaleFascicolo, Utente utente) throws SpagicClientException;

	LockedPratica creaBozza(String idDocumentaleFascicolo, String assegnatario, String mittente, List<String> destinatari, List<String> destinatariCC, String oggetto, String body, String firma,
			String replyTo, List<String> allegatiReplyTO, String messageIDReinoltro, Utente utente) throws SpagicClientException;

	LockedPratica creaBozzaDaTemplate(String idDocumentaleFascicolo, String idDocumentaleTemplate, Map<String, Object> campiTemplate, String assegnatario, String idDocumentaleReplyTO,
			Utente utente) throws SpagicClientException;

	LockedPratica modificaBozza(String idDocumentaleFascicolo, String idDocumentaleBozza, final String mittente, List<String> destinatari, List<String> destinatariCC, String oggetto, String body,
			String firma, Utente utente) throws SpagicClientException;
}
