package it.eng.consolepec.client;

import it.eng.cobo.consolepec.commons.atti.AllegatoAlfresco;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

/**
 *
 * @author biagiot
 *
 */
public interface DownloadAllegatoClient {

	AllegatoAlfresco downloadAllegato(Utente utente, String pathFile, String nomeFile, Float versione) throws SpagicClientException;

	AllegatoAlfresco downloadAllegato(Utente utente, String pathPratica, String nomeFile, String versione) throws SpagicClientException;

	AllegatoAlfresco downloadAllegatoSbustato(Utente utente, String pathFile, String nomeFile, Float versione) throws SpagicClientException;

}
