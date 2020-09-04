package it.eng.consolepec.client;

import it.eng.cobo.consolepec.commons.rubrica.Anagrafica;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

/**
 * @author GiacomoFM
 * @since 14/nov/2018
 */
public interface LagClient {

	Anagrafica importaAnagrafica(String cf, Utente utente) throws SpagicClientException;

}
