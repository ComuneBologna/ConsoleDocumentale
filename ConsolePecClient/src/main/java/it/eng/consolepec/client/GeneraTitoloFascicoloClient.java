package it.eng.consolepec.client;

import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.bean.request.fascicolo.FascicoloRequest;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

public interface GeneraTitoloFascicoloClient {

	String generaTitoloFascicoloDaTemplate(FascicoloRequest request, Utente utente) throws SpagicClientException;
}
