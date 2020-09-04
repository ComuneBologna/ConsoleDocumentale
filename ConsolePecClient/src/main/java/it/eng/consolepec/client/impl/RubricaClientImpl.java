package it.eng.consolepec.client.impl;

import java.util.Map;

import it.eng.cobo.consolepec.commons.rubrica.Anagrafica;
import it.eng.cobo.consolepec.commons.services.AnagraficaResponse;
import it.eng.cobo.consolepec.commons.services.ServiceResponse;
import it.eng.consolepec.client.AbstractConsolePecClient;
import it.eng.consolepec.client.ClientInvoker;
import it.eng.consolepec.client.RubricaClient;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class RubricaClientImpl extends AbstractConsolePecClient implements RubricaClient {

	public RubricaClientImpl(ClientInvoker clientInvoker) {
		super(clientInvoker);
	}

	@Override
	public ServiceResponse<AnagraficaResponse> ricerca(Map<String, Object> filtri, Integer limit, Integer offset, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.RICERCA_ANAGRAFICA, utente, filtri, limit, offset);
	}

	@Override
	public ServiceResponse<AnagraficaResponse> crea(Anagrafica anagrafica, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.CREA_ANAGRAFICA, utente, anagrafica);
	}

	@Override
	public ServiceResponse<AnagraficaResponse> modifica(Anagrafica anagrafica, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.MODIFICA_ANAGRAFICA, utente, anagrafica);
	}

	@Override
	public ServiceResponse<AnagraficaResponse> elimina(Anagrafica anagrafica, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.ELIMINA_ANAGRAFICA, utente, anagrafica);
	}

}
