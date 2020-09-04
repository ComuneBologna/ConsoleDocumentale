package it.eng.consolepec.client;

import it.eng.cobo.consolepec.commons.services.ServiceResponse;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

public interface EmissionePermessoClient {

	ServiceResponse<LockedPratica> emettiPermesso(Utente utente, String idPratica) throws SpagicClientException;

}
