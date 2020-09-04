package it.eng.consolepec.spagicclient;

import it.bologna.comune.spagic.interrogapg.Response;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

public interface SpagicClientInterrogaPG {

	/**
	 * 
	 * Cerca un documento sul sistema del protocollo (restituisce molte info rispetto al ricerca capofila)
	 * questo metodo deve essere utilizzato al di fuori della console documentale per specificare il codice utente
	 * 
	 * @param numeroPg
	 * @param annoPg
	 * @param codiceutente
	 * @param utente
	 * @return
	 * @throws SpagicClientException
	 */
	public Response cerca(String numeroPg, int annoPg, String codiceutente, Utente utente) throws SpagicClientException;
	
	/**
	 * 
	 * Cerca un documento sul sistema del protocollo (restituisce molte info rispetto al ricerca capofila)
	 * questo metodo deve essere utilizzato dalla console documentale, in quanto il codice utente e' preso da un file di propertie
	 * 
	 * @param numeroPg
	 * @param annoPg
	 * @param utente
	 * @return
	 * @throws SpagicClientException
	 */
	public Response cercaDocumentale(String numeroPg, int annoPg, Utente utente) throws SpagicClientException;
}
