package it.eng.consolepec.spagicclient;

import it.bologna.comune.alfresco.generic.service.Response;
import it.eng.consolepec.spagicclient.remoteproxy.ResponseWithAttachementsDto;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

public interface SpagicClientDownloadStampe {

	/**
	 * 
	 * @param fascicoloPath
	 * @param annoPG
	 * @param numPG
	 * @param utente
	 * @return
	 * @throws SpagicClientException
	 */
	public ResponseWithAttachementsDto<Response> downloadRiversamentoCartaceo(String praticaPath, String numPG, String annoPG, Utente utente) throws SpagicClientException;
	
	/**
	 * 
	 * @param praticaPath
	 * @param params
	 * @param utente
	 * @return
	 * @throws SpagicClientException
	 */
	public ResponseWithAttachementsDto<Response> downloadRicevuteDiConsegna(String praticaPath, Utente utente) throws SpagicClientException;

	
}
