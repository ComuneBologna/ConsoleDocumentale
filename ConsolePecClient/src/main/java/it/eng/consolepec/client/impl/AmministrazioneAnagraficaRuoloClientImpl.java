package it.eng.consolepec.client.impl;

import java.util.List;
import java.util.Map;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.Azione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.Abilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.services.AnagraficaRuoloResponse;
import it.eng.consolepec.client.AbstractConsolePecClient;
import it.eng.consolepec.client.AmministrazioneAnagraficaRuoloClient;
import it.eng.consolepec.client.ClientInvoker;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

/**
 *
 * @author biagiot
 *
 */
public class AmministrazioneAnagraficaRuoloClientImpl extends AbstractConsolePecClient implements AmministrazioneAnagraficaRuoloClient {

	public AmministrazioneAnagraficaRuoloClientImpl(ClientInvoker clientInvoker) {
		super(clientInvoker);
	}

	@Override
	public List<AnagraficaRuolo> ricerca(Map<String, Object> filtri, Integer limit, Integer offset, String orderBy, Boolean sort, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.RICERCA_ANAGRAFICA_GRUPPI, utente, filtri, limit, offset, orderBy, sort);
	}

	@Override
	public AnagraficaRuoloResponse crea(AnagraficaRuolo anagraficaRuolo, List<Abilitazione> abilitazioniRuolo, String settore, List<Azione> azioni, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.CREA_ANAGRAFICA_GRUPPI, utente, anagraficaRuolo, abilitazioniRuolo, settore, azioni);
	}

	@Override
	public AnagraficaRuoloResponse modifica(AnagraficaRuolo anagraficaRuolo, List<Abilitazione> abilitazioniRuolo, String nuovoSettore, List<Azione> azioni,
			Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.MODIFICA_ANAGRAFICA_GRUPPI, utente, anagraficaRuolo, abilitazioniRuolo, nuovoSettore, azioni);
	}

	@Override
	public long count(Map<String, Object> filtri, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.COUNT_ANAGRAFICA_GRUPPI, utente, filtri);
	}
}
