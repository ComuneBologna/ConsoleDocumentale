package it.eng.consolepec.spagicclient;

import java.util.List;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

/**
 *
 * @author roger
 *
 *         Interfaccia per la gestione delle operazioni sull'Iperfascicolo
 *
 */
public interface SpagicClientIperFascicoloHandler {

	/**
	 *
	 * @param nomeGruppo
	 * @param pathFascicolo
	 * @param operazioni
	 * @param utente
	 * @return
	 * @throws SpagicClientException
	 */
	public LockedPratica mergeCondivisione(String nomeGruppo, String pathFascicolo, List<String> operazioni, Utente utente) throws SpagicClientException;

	/**
	 *
	 * @param nomeGruppo
	 * @param utente
	 * @return
	 * @throws SpagicClientException
	 */
	public LockedPratica eliminaCondivisione(String nomeGruppo, String pathFascicolo, Utente utente) throws SpagicClientException;

	/**
	 *
	 * @param pathFascicolo
	 * @param utente
	 * @return
	 * @throws SpagicClientException
	 */
	public LockedPratica eliminaCollegamento(String pathFascicolo, String pathCollegamentoFascicoloDaEliminare, Utente utente) throws SpagicClientException;

}
