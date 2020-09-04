package it.eng.consolepec.client;

import java.util.List;
import java.util.Map;

import it.eng.cobo.consolepec.commons.services.PraticaProcediResponse;
import it.eng.cobo.consolepec.commons.services.ServiceResponse;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

/**
 * @author GiacomoFM
 * @since 06/nov/2017
 */
public interface IntegrazionePraticaProcediClient {

	ServiceResponse<PraticaProcediResponse> ricerca(Map<String, Object> filtri, Map<String, Boolean> sorts, Integer limit, Integer offset, Utente utente) throws SpagicClientException;

	ServiceResponse<PraticaProcediResponse> dettaglio(List<String> id, Utente utente) throws SpagicClientException;

	ServiceResponse<PraticaProcediResponse> countPratiche(Map<String, Object> filtri, Utente utente) throws SpagicClientException;

}
