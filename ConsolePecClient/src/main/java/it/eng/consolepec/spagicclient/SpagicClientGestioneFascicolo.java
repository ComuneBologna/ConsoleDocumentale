package it.eng.consolepec.spagicclient;

import it.bologna.comune.alfresco.generic.service.Response;
import it.eng.consolepec.spagicclient.bean.request.Protocollazione;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

import java.util.List;

/**
 * @author Roberto
 *
 */
/**
 * @author Roberto
 * 
 */
public interface SpagicClientGestioneFascicolo {

	/**
	 * @param pathFascicolo
	 * @param nuovoAssegnatario
	 * @param username
	 * @return
	 * @throws SpagicClientException
	 */
	public LockedPratica riassegna(String pathFascicolo, String nuovoAssegnatario, List<String> indirizziNotifica, String operatore, Utente utente) throws SpagicClientException;

	/**
	 * 
	 * @param pathFascicolo
	 * @param username
	 * @return
	 * @throws SpagicClientException
	 */
	public boolean elimina(String pathFascicolo, Utente utente) throws SpagicClientException;

	/**
	 * 
	 * @param pathFascicolo
	 * @param pathPratica
	 * @param username
	 * @return
	 * @throws SpagicClientException
	 */
	public Response aggiungiPratica(String pathFascicolo, String pathPratica, Utente utente) throws SpagicClientException;
	
	
	/**
	 * 
	 * @param pathFascicolo
	 * @param numpg
	 * @param annopg
	 * @return
	 * @throws SpagicClientException
	 */
	public LockedPratica aggiornaPG(String pathFascicolo, List<Protocollazione> protocollazioni, Utente utente) throws SpagicClientException;

}
