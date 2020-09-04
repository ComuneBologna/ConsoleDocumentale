package it.eng.consolepec.spagicclient.remoteproxy.impl;

import javax.xml.bind.JAXBException;

import it.bologna.comune.alfresco.protocollazione.Request;
import it.bologna.comune.alfresco.protocollazione.Response;
import it.eng.consolepec.spagicclient.SpagicClientGetTipoPraticaProtocollazione;
import it.eng.consolepec.spagicclient.remoteproxy.abs.AbstractSpagicClientRemoteProxy;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientErrorCode;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class SpagicClientGetTipoPraticaProtocollazioneRemoteProxy extends AbstractSpagicClientRemoteProxy<Request, Response> implements SpagicClientGetTipoPraticaProtocollazione {

	protected SpagicClientGetTipoPraticaProtocollazioneRemoteProxy(String serviceProxyUrl, String alfrescoUsername, String alfrescoPassword, String serviceUsername, String servicePassword,
			String restServiceUrl) {
		super(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	@Override
	public Response getTipoPratica(Request request) throws SpagicClientException {
		Response response = invokeSpagicService(request);
		if ("ERR".equalsIgnoreCase(response.getCode()))
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		return response;
	}

	@Override
	protected String getSpagicServiceId() {
		return ServiceNamesUtil.ELENCO_PRATICA_PROTOCOLLAZIONE;
	}

	@Override
	protected String getJaxbRequestToXml(Request request) throws JAXBException {
		return SpagicClientSerializationUtil.getElencoProtocollazioneRequestToString(request);
	}

	@Override
	protected Response getXmlResponseToJaxb(String response) throws JAXBException {
		return SpagicClientSerializationUtil.getElencoProtocollazioneResponseToObject(response);
	}

}
