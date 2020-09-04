package it.eng.consolepec.client.impl;

import java.util.List;
import java.util.Map;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.consolepec.client.AbstractConsolePecClient;
import it.eng.consolepec.client.AmministrazioneAnagraficaFascicoloClient;
import it.eng.consolepec.client.ClientInvoker;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

/**
 *
 * @author biagiot
 *
 */
public class AmministrazioneAnagraficaFascicoloClientImpl extends AbstractConsolePecClient implements AmministrazioneAnagraficaFascicoloClient {

	public AmministrazioneAnagraficaFascicoloClientImpl(ClientInvoker clientInvoker) {
		super(clientInvoker);
	}

	@Override
	public List<AnagraficaFascicolo> ricerca(Map<String, Object> filtri, Integer limit, Integer offset, String orderBy, Boolean sort, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.RICERCA_ANAGRAFICA_FASCICOLO, utente, filtri, limit, offset, orderBy, sort);
	}

	@Override
	public AnagraficaFascicolo crea(AnagraficaFascicolo anagraficaFascicolo, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.CREA_ANAGRAFICA_FASCICOLO, utente, anagraficaFascicolo);
	}

	@Override
	public AnagraficaFascicolo modifica(AnagraficaFascicolo anagraficaFascicolo, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.MODIFICA_ANAGRAFICA_FASCICOLO, utente, anagraficaFascicolo);
	}

	@Override
	public long count(Map<String, Object> filtri, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.COUNT_ANAGRAFICA_FASCICOLO, utente, filtri);
	}

}
