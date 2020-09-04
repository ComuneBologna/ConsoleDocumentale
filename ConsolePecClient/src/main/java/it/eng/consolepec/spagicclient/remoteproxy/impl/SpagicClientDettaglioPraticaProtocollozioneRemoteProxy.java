package it.eng.consolepec.spagicclient.remoteproxy.impl;

import javax.xml.bind.JAXBException;

import it.bologna.comune.spagic.protocollazione.dettaglio.Request;
import it.bologna.comune.spagic.protocollazione.dettaglio.Response;
import it.eng.consolepec.spagicclient.SpagicClientDettaglioPraticaProtocollozione;
import it.eng.consolepec.spagicclient.remoteproxy.abs.AbstractSpagicClientRemoteProxy;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientErrorCode;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class SpagicClientDettaglioPraticaProtocollozioneRemoteProxy extends AbstractSpagicClientRemoteProxy<Request, Response> implements SpagicClientDettaglioPraticaProtocollozione {

	protected SpagicClientDettaglioPraticaProtocollozioneRemoteProxy(String serviceProxyUrl, String alfrescoUsername, String alfrescoPassword, String serviceUsername, String servicePassword,
			String restServiceUrl) {
		super(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	@Override
	public Response getDettaglioPratica(String identificativoPratica) throws SpagicClientException {
		Request request = new Request();
		request.setId(identificativoPratica);

		Response response = invokeSpagicService(request);
		if (response == null)
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		return response;
	}

	@Override
	protected String getSpagicServiceId() {
		return ServiceNamesUtil.DETTAGLIO_PRATICA_PROTOCOLLAZIONE;
	}

	@Override
	protected String getJaxbRequestToXml(Request request) throws JAXBException {
		return SpagicClientSerializationUtil.getProtocollazioneDettaglioRequestToXml(request);
	}

	@Override
	protected Response getXmlResponseToJaxb(String response) throws JAXBException {
		return SpagicClientSerializationUtil.getProtocollazioneDettaglioResponseToObject(response);
	}

}
