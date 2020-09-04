package it.eng.consolepec.spagicclient;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

public interface SpagicClientGestioneElettorale {
	
	public void importa(String pathPratica, Utente utente) throws SpagicClientException;
	
	public void annulla(String pathPratica, Utente utente) throws SpagicClientException;

}
