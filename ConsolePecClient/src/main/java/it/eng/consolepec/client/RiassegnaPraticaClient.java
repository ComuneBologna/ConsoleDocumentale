package it.eng.consolepec.client;

import java.util.List;

import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

/**
 *
 * @author biagiot
 *
 */
public interface RiassegnaPraticaClient {

	public LockedPratica riassegna(String idDocumentale, String nuovoAssegnatario, List<String> indirizziNotifica, String operatore, String note, Utente utente) throws SpagicClientException;

}
