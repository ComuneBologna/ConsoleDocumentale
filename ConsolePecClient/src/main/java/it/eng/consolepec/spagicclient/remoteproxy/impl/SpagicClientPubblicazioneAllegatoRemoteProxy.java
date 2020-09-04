package it.eng.consolepec.spagicclient.remoteproxy.impl;

import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

import it.bologna.comune.alfresco.generic.service.Request;
import it.bologna.comune.alfresco.generic.service.Response;
import it.eng.cobo.consolepec.util.date.DateUtils;
import it.eng.consolepec.spagicclient.SpagicClientPubblicazioneAllegato;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.abs.AbstractSpagicClientRemoteProxy;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientRemoteProxyUtil;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class SpagicClientPubblicazioneAllegatoRemoteProxy extends AbstractSpagicClientRemoteProxy<Request, Response> implements SpagicClientPubblicazioneAllegato {

	protected SpagicClientPubblicazioneAllegatoRemoteProxy(String serviceProxyUrl, String alfrescoUsername, String alfrescoPassword, String serviceUsername, String servicePassword,
			String restServiceUrl) {
		super(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	@Override
	public LockedPratica pubblicaAllegato(String praticaPath, String allegatoNome, Date dataInizio, Date dataFine, List<String> destinatari, String testo, Utente utente) throws SpagicClientException {
		Request genericRequest = new Request();
		genericRequest.setAlfrescopassword(getAlfrescoPassword());
		genericRequest.setAlfrescousername(getAlfrescoUsername());
		genericRequest.setServicename(ServiceNamesUtil.PUBBLICAZIONE_ALLEGATO);
		it.bologna.comune.spagic.pubblicazioneallegato.Request myRequest = new it.bologna.comune.spagic.pubblicazioneallegato.Request();
		myRequest.setPraticaPath(praticaPath);
		myRequest.setNomeAllegato(allegatoNome);
		myRequest.setDataInizio(DateUtils.dateToXMLGrCal(dataInizio));
		myRequest.setDataFine(DateUtils.dateToXMLGrCal(dataFine));
		myRequest.getDestinatari().addAll(destinatari);
		myRequest.setTestoEmail(testo);
		genericRequest.setRequestparam(SpagicClientSerializationUtil.getRequestPubblicazioneAllegatoToString(myRequest));
		genericRequest.setUtente(SpagicClientRemoteProxyUtil.convert(utente));
		Response response = invokeSpagicService(genericRequest);
		if (response.getError() != null) throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());
		return new LockedPratica(response.getResponseparam(), response.getApplicationxml());
	}

	@Override
	public LockedPratica rimuoviPubblicazioneAllegato(String praticaPath, String allegatoNome, Utente utente) throws SpagicClientException {
		Request genericRequest = new Request();
		genericRequest.setAlfrescopassword(getAlfrescoPassword());
		genericRequest.setAlfrescousername(getAlfrescoUsername());
		genericRequest.setServicename(ServiceNamesUtil.RIMOZIONE_PUBBLICAZIONE_ALLEGATO);
		it.bologna.comune.spagic.rimozionepubblicazioneallegato.Request myRequest = new it.bologna.comune.spagic.rimozionepubblicazioneallegato.Request();
		myRequest.setPraticaPath(praticaPath);
		myRequest.setNomeAllegato(allegatoNome);
		genericRequest.setRequestparam(SpagicClientSerializationUtil.getRequestRimozionePubblicazioneAllegatoToString(myRequest));
		genericRequest.setUtente(SpagicClientRemoteProxyUtil.convert(utente));
		Response response = invokeSpagicService(genericRequest);
		if (response.getError() != null) throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());
		return new LockedPratica(response.getResponseparam(), response.getApplicationxml());
	}

	@Override
	protected String getJaxbRequestToXml(Request request) throws JAXBException {
		// return SpagicClientSerializationUtil.getRequestPubblicazioneAllegatoToString(request);
		return SpagicClientSerializationUtil.getRequestToString(request);
	}

	@Override
	protected Response getXmlResponseToJaxb(String response) throws JAXBException {
		// return SpagicClientSerializationUtil.getResponsePubblicazioneAllegatoToObject(response);
		return SpagicClientSerializationUtil.getResponseXmlToObject(response);
	}

	@Override
	protected String getSpagicServiceId() {
		return ServiceNamesUtil.CONSOLE_PEC_DISPATCHER;
	}

}
