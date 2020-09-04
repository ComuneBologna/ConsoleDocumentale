package it.eng.consolepec.spagicclient.remoteproxy.impl;

import java.util.List;

import javax.xml.bind.JAXBException;

import it.bologna.comune.alfresco.generic.service.Request;
import it.bologna.comune.alfresco.generic.service.Response;
import it.bologna.comune.spagic.aggiuntadatiaggiuntivi.Request.Valori;
import it.bologna.comune.spagic.aggiuntadatiaggiuntivi.Request.Valori.Valore;
import it.bologna.comune.spagic.gestione.datiaggiuntivi.DatoAgg;
import it.bologna.comune.spagic.gestione.datiaggiuntivi.ValidazioneDatoAgg;
import it.eng.consolepec.spagicclient.SpagicClientDatiAggiuntivi;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.bean.request.datiaggiuntivi.DatoAggiuntivo;
import it.eng.consolepec.spagicclient.bean.response.validazione.ValidazioneDatoAggiuntivo;
import it.eng.consolepec.spagicclient.bean.response.validazione.ValidazioneEdErroriDatiAggiuntivi;
import it.eng.consolepec.spagicclient.remoteproxy.abs.AbstractSpagicClientRemoteProxy;
import it.eng.consolepec.spagicclient.remoteproxy.abs.NewSpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientRemoteProxyUtil;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.spagicclient.remoteproxy.util.DatiAggiuntiviUtil;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class SpagicClientDatiAggiuntiviRemoteProxy extends AbstractSpagicClientRemoteProxy<Request, Response> implements SpagicClientDatiAggiuntivi {

	protected SpagicClientDatiAggiuntiviRemoteProxy(String serviceProxyUrl, String alfrescoUsername, String alfrescoPassword, String serviceUsername, String servicePassword, String restServiceUrl) {
		super(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	@Override
	public LockedPratica aggiungiDatiAggiuntivi(String pathPratica, List<DatoAggiuntivo> valoriAggiuntivi, Utente utente) throws SpagicClientException {
		Request genericRequest = new Request();
		genericRequest.setAlfrescopassword(getAlfrescoPassword());
		genericRequest.setAlfrescousername(getAlfrescoUsername());
		genericRequest.setServicename(ServiceNamesUtil.AGGIUNTA_DATI_AGGIUNTIVI);
		genericRequest.setUtente(SpagicClientRemoteProxyUtil.convert(utente));

		it.bologna.comune.spagic.aggiuntadatiaggiuntivi.Request request = new it.bologna.comune.spagic.aggiuntadatiaggiuntivi.Request();
		request.setPraticapath(pathPratica);
		request.setValori(new Valori());
		for (DatoAggiuntivo va : valoriAggiuntivi) {
			Valore valore = DatiAggiuntiviUtil.datoSpagicToDatoAggiunta(va);
			request.getValori().getValore().add(valore);
		}

		String xmlRequest = SpagicClientSerializationUtil.getRequestAggiuntaDatiAggiuntiviToString(request);

		genericRequest.setRequestparam(xmlRequest);

		Response response = invokeSpagicService(genericRequest);

		if (response.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());

		it.bologna.comune.spagic.aggiuntadatiaggiuntivi.Response responseParam = SpagicClientSerializationUtil.getResponseAggiuntaDatiAggiuntiviToObject(response.getResponseparam());

		return new LockedPratica(responseParam.getLockedPratica().getHashPratica(), responseParam.getLockedPratica().getXmlpratica());
	}

	@Override
	public LockedPratica rimuoviDatiAggiuntivi(String pathPratica, List<String> nomiCampi, Utente utente) throws SpagicClientException {
		Request genericRequest = new Request();
		genericRequest.setAlfrescopassword(getAlfrescoPassword());
		genericRequest.setAlfrescousername(getAlfrescoUsername());
		genericRequest.setServicename(ServiceNamesUtil.RIMOZIONE_DATI_AGGIUNTIVI);
		genericRequest.setUtente(SpagicClientRemoteProxyUtil.convert(utente));

		it.bologna.comune.spagic.rimozionedatiaggiuntivi.Request request = new it.bologna.comune.spagic.rimozionedatiaggiuntivi.Request();
		request.setPraticapath(pathPratica);
		request.getDati().addAll(nomiCampi);

		String xmlRequest = SpagicClientSerializationUtil.getRequestRimozioneDatiAggiuntiviToString(request);

		genericRequest.setRequestparam(xmlRequest);

		Response response = invokeSpagicService(genericRequest);

		if (response.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());

		it.bologna.comune.spagic.rimozionedatiaggiuntivi.Response responseParam = SpagicClientSerializationUtil.getResponseRimozioneDatiAggiuntiviToObject(response.getResponseparam());

		return new LockedPratica(responseParam.getLockedPratica().getHashPratica(), responseParam.getLockedPratica().getXmlpratica());
	}

	@Override
	public ValidazioneEdErroriDatiAggiuntivi validaDatiAggiuntivi(List<DatoAggiuntivo> valoriAggiuntivi, Utente utente) throws SpagicClientException {
		Request genericRequest = new Request();
		genericRequest.setAlfrescopassword(getAlfrescoPassword());
		genericRequest.setAlfrescousername(getAlfrescoUsername());
		genericRequest.setServicename(ServiceNamesUtil.VALIDAZIONE_DATI_AGGIUNTIVI);
		genericRequest.setUtente(SpagicClientRemoteProxyUtil.convert(utente));

		it.bologna.comune.spagic.gestione.datiaggiuntivi.Request request = new it.bologna.comune.spagic.gestione.datiaggiuntivi.Request();

		for (DatoAggiuntivo va : valoriAggiuntivi) {
			DatoAgg valore = DatiAggiuntiviUtil.datoSpagicToDatoGestione(va);
			request.getDatoAgg().add(valore);
		}

		String xmlRequest = NewSpagicClientSerializationUtil.serialize(request);
		genericRequest.setRequestparam(xmlRequest);

		Response response = invokeSpagicService(genericRequest);

		if (response.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());

		it.bologna.comune.spagic.gestione.datiaggiuntivi.Response internalResponse = NewSpagicClientSerializationUtil.deserialize(response.getResponseparam(),
				it.bologna.comune.spagic.gestione.datiaggiuntivi.Response.class);

		ValidazioneEdErroriDatiAggiuntivi validazioneEdErrori = new ValidazioneEdErroriDatiAggiuntivi();

		for (ValidazioneDatoAgg va : internalResponse.getValidazioneDatoAgg()) {
			ValidazioneDatoAggiuntivo validazione = new ValidazioneDatoAggiuntivo(DatiAggiuntiviUtil.datoGestioneToDatoSpagic(va.getDatoAgg()), va.isValido());
			validazione.getVieConLoStessoNome().addAll(va.getVieConLoStessoNomeSit());
			validazioneEdErrori.getValidazione().add(validazione);
		}

		validazioneEdErrori.getErrori().addAll(internalResponse.getErrori());

		return validazioneEdErrori;
	}

	@Override
	protected String getJaxbRequestToXml(Request request) throws JAXBException {
		return SpagicClientSerializationUtil.getRequestToString(request);
	}

	@Override
	protected Response getXmlResponseToJaxb(String response) throws JAXBException {
		return SpagicClientSerializationUtil.getResponseXmlToObject(response);
	}

	@Override
	protected String getSpagicServiceId() {
		return ServiceNamesUtil.CONSOLE_PEC_DISPATCHER;
	}
}
