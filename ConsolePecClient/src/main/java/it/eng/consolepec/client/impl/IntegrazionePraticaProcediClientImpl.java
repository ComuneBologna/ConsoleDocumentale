package it.eng.consolepec.client.impl;

import java.util.List;
import java.util.Map;

import it.eng.cobo.consolepec.commons.services.PraticaProcediResponse;
import it.eng.cobo.consolepec.commons.services.ServiceResponse;
import it.eng.consolepec.client.AbstractConsolePecClient;
import it.eng.consolepec.client.ClientInvoker;
import it.eng.consolepec.client.IntegrazionePraticaProcediClient;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

/**
 * @author GiacomoFM
 * @since 06/nov/2017
 */
public class IntegrazionePraticaProcediClientImpl extends AbstractConsolePecClient implements IntegrazionePraticaProcediClient {

	public IntegrazionePraticaProcediClientImpl(ClientInvoker clientInvoker) {
		super(clientInvoker);
	}

	@Override
	public ServiceResponse<PraticaProcediResponse> ricerca(Map<String, Object> filtri, Map<String, Boolean> sorts, Integer limit, Integer offset, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.RICERCA_PRATICA_PROCEDI, utente, filtri, sorts, limit, offset);
	}

	@Override
	public ServiceResponse<PraticaProcediResponse> countPratiche(Map<String, Object> filtri, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.COUNT_PRATICA_PROCEDI, utente, filtri);
	}

	@Override
	public ServiceResponse<PraticaProcediResponse> dettaglio(List<String> id, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.DETTAGLIO_PRATICA_PROCEDI, utente, id);
	}

}
