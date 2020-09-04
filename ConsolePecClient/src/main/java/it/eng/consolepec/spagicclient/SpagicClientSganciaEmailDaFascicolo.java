package it.eng.consolepec.spagicclient;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

public interface SpagicClientSganciaEmailDaFascicolo {

	public LockedPratica sganciaEmail(String pathEmail, String pathFascicolo, Utente utente) throws SpagicClientException;

}
