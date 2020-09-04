package it.eng.consolepec.client.impl;

import java.util.List;
import java.util.Map;

import it.eng.cobo.consolepec.commons.configurazioni.AbilitazioniRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.util.AbilitazioneRuoloSingola;
import it.eng.consolepec.client.AbstractConsolePecClient;
import it.eng.consolepec.client.AmministrazioneAbilitazioniRuoloClient;
import it.eng.consolepec.client.ClientInvoker;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

/**
 *
 * @author biagiot
 *
 */
public class AmministrazioneAbilitazioniRuoloClientImpl extends AbstractConsolePecClient implements AmministrazioneAbilitazioniRuoloClient {

	public AmministrazioneAbilitazioniRuoloClientImpl(ClientInvoker clientInvoker) {
		super(clientInvoker);
	}

	@Override
	public List<AbilitazioniRuolo> carica(Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.CARICA_ABILITAZIONI_RUOLI, utente);
	}

	@Override
	public List<AbilitazioneRuoloSingola> aggregate(Map<String, Object> filters, Integer limit, Integer offset, String orderBy, Boolean sort, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.AGGREGATE_ABILITAZIONI_RUOLI, utente, filters, limit, offset, orderBy, sort);
	}

	@Override
	public int countAggregate(Map<String, Object> filters, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.COUNT_AGGREGATE_ABILITAZIONI_RUOLI, utente, filters);
	}
}
