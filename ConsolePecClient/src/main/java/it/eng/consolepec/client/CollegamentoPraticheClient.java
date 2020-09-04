package it.eng.consolepec.client;

import it.eng.cobo.consolepec.commons.pratica.fascicolo.collegamenti.Permessi;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

/**
 * @author GiacomoFM
 * @since 08/nov/2018
 */
public interface CollegamentoPraticheClient {

	LockedPratica updateCollegamento(String pathPraticaSorgente, String pathPraticaRemota, Permessi permessi, Utente utente) throws SpagicClientException;

	LockedPratica updateCollegamentoGruppo(String pathPraticaSorgente, String pathPraticaRemota, Permessi permessi, String ruolo, Utente utente) throws SpagicClientException;

}
