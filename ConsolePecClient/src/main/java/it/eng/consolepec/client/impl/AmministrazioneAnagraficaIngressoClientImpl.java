package it.eng.consolepec.client.impl;

import java.util.List;
import java.util.Map;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaIngresso;
import it.eng.cobo.consolepec.commons.configurazioni.services.AnagraficaIngressoResponse;
import it.eng.consolepec.client.AbstractConsolePecClient;
import it.eng.consolepec.client.AmministrazioneAnagraficaIngressoClient;
import it.eng.consolepec.client.ClientInvoker;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

/**
 *
 * @author biagiot
 *
 */
public class AmministrazioneAnagraficaIngressoClientImpl extends AbstractConsolePecClient implements AmministrazioneAnagraficaIngressoClient {

	public AmministrazioneAnagraficaIngressoClientImpl(ClientInvoker clientInvoker) {
		super(clientInvoker);
	}

	@Override
	public List<AnagraficaIngresso> ricerca(Map<String, Object> filtri, Integer limit, Integer offset, String orderBy, Boolean sort, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.RICERCA_ANAGRAFICA_INGRESSI, utente, filtri, limit, offset, orderBy, sort);
	}

	@Override
	public AnagraficaIngressoResponse crea(AnagraficaIngresso anagraficaIngresso, boolean creaEmailOut, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.CREA_ANAGRAFICA_INGRESSI, utente, anagraficaIngresso, creaEmailOut);

	}

	@Override
	public AnagraficaIngressoResponse modifica(String indirizzoEmail, AnagraficaIngresso anagraficaIngresso, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.MODIFICA_ANAGRAFICA_INGRESSI, utente, indirizzoEmail, anagraficaIngresso);
	}

	@Override
	public long count(Map<String, Object> filtri, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.COUNT_ANAGRAFICA_INGRESSI, utente, filtri);
	}

	@Override
	public AnagraficaIngresso aggiornaPrimoAssegnatario(String tipologiaEmail, String indirizzoEmail, String nuovoAssegnatario, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.AGGIORNA_PRIMO_ASSEGNATARIO, utente, tipologiaEmail, indirizzoEmail, nuovoAssegnatario);

	}

}
