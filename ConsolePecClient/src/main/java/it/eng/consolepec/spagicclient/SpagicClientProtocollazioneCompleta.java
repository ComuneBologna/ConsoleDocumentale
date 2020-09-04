package it.eng.consolepec.spagicclient;

import it.bologna.comune.alfresco.generic.service.Response;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.CreaFascicoloDto;

public interface SpagicClientProtocollazioneCompleta {

	/**
	 * 
	 * @param pathPratiche
	 * @param allegati
	 * @param pathFascicolo
	 * @param request
	 * @param username
	 * @return
	 * @throws SpagicClientException
	 */
	public Response protocolla(String[] pathPratiche, String[] allegati, String pathFascicolo, String request, Utente utente) throws SpagicClientException;

	/**
	 * 
	 * @param pathPratiche
	 * @param allegati
	 * @param pathFascicolo
	 * @param request
	 * @param numeroCapofila
	 * @param annoCapofila
	 * @param username
	 * @return
	 * @throws SpagicClientException
	 */
	public Response protocolla(String[] pathPratiche, String[] allegati, String pathFascicolo, String request, String numeroCapofila, String annoCapofila, Utente utente) throws SpagicClientException;

	/**
	 * 
	 * @param pathPratiche
	 * @param allegati
	 * @param pathFascicolo
	 * @param request
	 * @param numeroCapofila
	 * @param annoCapofila
	 * @param utente
	 * @return
	 * @throws SpagicClientException
	 */
	public Response protocollaFromBA01(String[] pathPratiche, String[] allegati, String pathFascicolo, String request, String numeroCapofila, String annoCapofila, Utente utente) throws SpagicClientException;

	/**
	 * 
	 * @param creaFascicoloDto
	 * @param request
	 * @param pathEmailIn
	 * @param utente
	 * @return
	 * @throws SpagicClientException
	 */
	public Response protocollaFascicoloNuovo(CreaFascicoloDto creaFascicoloDto, String request, String pathEmailIn, Utente utente) throws SpagicClientException;

	/**
	 * 
	 * @param creaFascicoloDto
	 * @param request
	 * @param pathEmailIn
	 * @param numeroCapofila
	 * @param annoCapofila
	 * @param utente
	 * @return
	 * @throws SpagicClientException
	 */
	public Response protocollaFascicoloNuovo(CreaFascicoloDto creaFascicoloDto, String request, String pathEmailIn, String numeroCapofila, String annoCapofila, Utente utente) throws SpagicClientException;

	/**
	 * 
	 * @param creaFascicoloDto
	 * @param request
	 * @param pathEmailIn
	 * @param numeroCapofila
	 * @param annoCapofila
	 * @param utente
	 * @return
	 * @throws SpagicClientException
	 */
	public Response protocollaFascicoloNuovoFromBA01(CreaFascicoloDto creaFascicoloDto, String request, String pathEmailIn, String numeroCapofila, String annoCapofila, Utente utente) throws SpagicClientException;
}
