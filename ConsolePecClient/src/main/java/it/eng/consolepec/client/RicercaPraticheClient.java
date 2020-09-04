package it.eng.consolepec.client;

import java.util.List;
import java.util.Map;

import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

/**
 *
 * @author biagiot
 *
 */
public interface RicercaPraticheClient {

	public List<LockedPratica> ricercaPratiche(Map<String, Object> filters, Map<String, Integer> sorts, Integer limit, Integer offset, Utente utente) throws SpagicClientException;
}
