package it.eng.consolepec.spagicclient;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

import java.io.InputStream;
import java.util.List;

public interface SpagicClientEstrazioneAmianto {

	/**
	 * 
	 * @param nomeEstrazione
	 * @param filters
	 * @return
	 * @throws SpagicClientException
	 */
	InputStream estrai(String nomeEstrazione, List<Object> filtri, Utente utente)	throws SpagicClientException;
}
