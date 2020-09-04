package it.eng.consolepec.spagicclient.remoteproxy.impl;

import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBException;

import it.bologna.comune.alfresco.generic.service.Request;
import it.bologna.comune.alfresco.generic.service.Response;
import it.bologna.comune.mongodb.estrazioni.amianto.EstrazioneAmiantoRequest;
import it.bologna.comune.mongodb.estrazioni.amianto.EstrazioneAmiantoResponse;
import it.eng.consolepec.spagicclient.SpagicClientEstrazioneAmianto;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.ResponseWithAttachementsDto;
import it.eng.consolepec.spagicclient.remoteproxy.abs.AbstractSpagicClientRemoteProxy;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientRemoteProxyUtil;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientErrorCode;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class SpagicClientEstrazioneAmiantoRemoteProxy extends AbstractSpagicClientRemoteProxy<Request, Response> implements SpagicClientEstrazioneAmianto {

	protected SpagicClientEstrazioneAmiantoRemoteProxy(String serviceProxyUrl, String alfrescoUsername, String alfrescoPassword, String serviceUsername, String servicePassword,
			String restServiceUrl) {
		super(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	@Override
	public InputStream estrai(String nomeEstrazione, List<Object> filtri, Utente utente) throws SpagicClientException {
		Request request = new Request();
		request.setAlfrescopassword(getAlfrescoPassword());
		request.setAlfrescousername(getAlfrescoUsername());
		request.setServicename(ServiceNamesUtil.ESTRAZIONEAMIANTO);
		request.setUtente(SpagicClientRemoteProxyUtil.convert(utente));

		EstrazioneAmiantoRequest estrazioneAmiantoRequest = new EstrazioneAmiantoRequest();
		estrazioneAmiantoRequest.setTipoEstrazione(nomeEstrazione);

		estrazioneAmiantoRequest.getFilters().addAll(filtri);

		try {
			request.setInternalrequestparam(SpagicClientSerializationUtil.getCreateEstrazioneAmiantoRequestToString(estrazioneAmiantoRequest));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		ResponseWithAttachementsDto<Response> response = invokeSpagicServiceWhitAttachementResponse(request);

		EstrazioneAmiantoResponse responseEstrazioneAmiantoToObject = SpagicClientSerializationUtil.getResponseEstrazioneAmiantoToObject(response.getResponse().getResponseparam());
		if (response.getResponse().getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getResponse().getError());
		if (!responseEstrazioneAmiantoToObject.getResult().equals("OK")) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
		return response.getAttachements().values().iterator().next();
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
