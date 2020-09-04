package it.eng.consolepec.client;

import java.util.List;

import it.eng.cobo.consolepec.commons.services.InputStreamMapper;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

public interface ClientInvoker {

	<T> T invoke(String service, Utente utente, Object... request) throws SpagicClientException;

	<T> T invoke(String service, Utente utente, List<InputStreamMapper> streams, Object... request) throws SpagicClientException;

}
