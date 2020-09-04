package it.eng.consolepec.client.impl;

import java.util.List;
import java.util.Map;

import it.eng.consolepec.client.AbstractConsolePecClient;
import it.eng.consolepec.client.ClientInvoker;
import it.eng.consolepec.client.RicercaPraticheClient;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

/**
 *
 * @author biagiot
 *
 */
public class RicercaPraticheClientImpl extends AbstractConsolePecClient implements RicercaPraticheClient {

	public RicercaPraticheClientImpl(ClientInvoker clientInvoker) {
		super(clientInvoker);
	}

	@Override
	public List<LockedPratica> ricercaPratiche(Map<String, Object> filters, Map<String, Integer> sorts, Integer limit, Integer offset, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.RICERCA_PRATICHE_EXT, utente, filters, sorts, limit, offset);
	}

}
