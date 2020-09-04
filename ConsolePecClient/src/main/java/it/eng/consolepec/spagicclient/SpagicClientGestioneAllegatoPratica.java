package it.eng.consolepec.spagicclient;

import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

import java.io.File;
import java.util.List;

public interface SpagicClientGestioneAllegatoPratica {
	/**
	 * 
	 * @param pathPratica
	 * @param nomeFile
	 * @param allegato
	 * @param nomeUtente
	 * @param username
	 * @return
	 * @throws SpagicClientException
	 */
	public LockedPratica uploadFile(String pathPratica, String nomeFile, File allegato, Utente utente) throws SpagicClientException;
	
	/**
	 * 
	 * @param pathPratica
	 * @param nomeFile
	 * @param tipologia
	 * @param allegato
	 * @param utente
	 * @return
	 * @throws SpagicClientException
	 */
	public LockedPratica uploadFile(String pathPratica, String nomeFile, String tipologia, File allegato, Utente utente) throws SpagicClientException;

	/**
	 * 
	 * @param pathPratica
	 * @param nomeFile
	 * @param tipologia
	 * @param datiAggiuntivi
	 * @param allegato
	 * @param utente
	 * @return
	 * @throws SpagicClientException
	 */
	public LockedPratica uploadFile(String pathPratica, String nomeFile, String tipologia, List<DatoAggiuntivo> datiAggiuntivi, File allegato, Utente utente) throws SpagicClientException;
	
	/**
	 * 
	 * @param pathPraticaDest
	 * @param pathPraticaSorgente
	 * @param nomeFileDaAllegare
	 * @param nomeUtente
	 * @param username
	 * @return
	 * @throws SpagicClientException
	 */
	public LockedPratica uploadFile(String pathPraticaDest, String pathPraticaSorgente, String nomeFileDaAllegare, Utente utente) throws SpagicClientException;

	/**
	 * 
	 * @param pathPratica
	 * @param nomeFile
	 * @param username
	 * @return
	 * @throws SpagicClientException
	 */
	public LockedPratica removeFile(String pathPratica, String nomeFile, Utente utente) throws SpagicClientException;

	/**
	 * 
	 * @param pathPratica
	 * @param nomiFiles
	 * @param username
	 * @return
	 * @throws SpagicClientException
	 */
	public LockedPratica removeFiles(String pathPratica, List<String> nomiFiles, Utente utente) throws SpagicClientException;
	
	/**
	 * 
	 * @param pathPratica
	 * @param nomiFiles
	 * @param username
	 * @return
	 * @throws SpagicClientException
	 */
	public LockedPratica cambiaTipologiaAllegato(String pathPratica, List<String> nomiFiles, String tipologiaAllegato, Utente utente) throws SpagicClientException;


}
