package it.eng.consolepec.spagicclient.remoteproxy.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import it.bologna.comune.alfresco.generic.service.Request;
import it.bologna.comune.alfresco.generic.service.Response;
import it.bologna.comune.spagic.gestione.metadati.MetadatoEtichettaMap;
import it.eng.consolepec.spagicclient.SpagicClientGestioneMetadatiPratica;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.abs.AbstractSpagicClientRemoteProxy;
import it.eng.consolepec.spagicclient.remoteproxy.abs.NewSpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientRemoteProxyUtil;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

/**
 *
 * @author biagiot
 *
 */
public class SpagicClientGestioneMetadatiPraticaRemoteProxy extends AbstractSpagicClientRemoteProxy<Request, Response> implements SpagicClientGestioneMetadatiPratica {

	protected SpagicClientGestioneMetadatiPraticaRemoteProxy(String serviceProxyUrl, String alfrescoUsername, String alfrescoPassword, String serviceUsername, String servicePassword,
			String restServiceUrl) {
		super(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	@Override
	public Map<String, String> getEtichetteMetadatiMap(String tipoPratica, Utente utente) throws SpagicClientException {
		Request genericRequest = new Request();
		genericRequest.setAlfrescopassword(getAlfrescoPassword());
		genericRequest.setAlfrescousername(getAlfrescoUsername());
		genericRequest.setPathapplicationxml("");
		genericRequest.setServicename(ServiceNamesUtil.ESTRAI_ETICHETTE_METADATI_PRATICA);
		genericRequest.setUtente(SpagicClientRemoteProxyUtil.convert(utente));
		it.bologna.comune.spagic.gestione.metadati.Request request = new it.bologna.comune.spagic.gestione.metadati.Request();
		request.setTipoPratica(tipoPratica);
		genericRequest.setRequestparam(NewSpagicClientSerializationUtil.serialize(request));

		Response genericResponse = invokeSpagicService(genericRequest);
		if (genericResponse.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(genericResponse.getError());

		String responseParam = genericResponse.getResponseparam();
		it.bologna.comune.spagic.gestione.metadati.Response response = NewSpagicClientSerializationUtil.deserialize(responseParam, it.bologna.comune.spagic.gestione.metadati.Response.class);

		Map<String, String> result = new LinkedHashMap<String, String>();
		for (MetadatoEtichettaMap mem : response.getMetadatoEtichettaMap())
			result.put(mem.getIdMetadato(), mem.getEtichetta());

		return result;
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
