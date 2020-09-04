package it.eng.consolepec.spagicclient.remoteproxy.impl;

import javax.xml.bind.JAXBException;

import it.bologna.comune.alfresco.generic.service.Request;
import it.bologna.comune.alfresco.generic.service.Response;
import it.eng.consolepec.spagicclient.SpagicClientInterrogaPG;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.abs.AbstractSpagicClientRemoteProxy;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientRemoteProxyUtil;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientErrorCode;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class SpagicClientInterrogaPGRemoteProxy extends AbstractSpagicClientRemoteProxy<Request, Response> implements SpagicClientInterrogaPG {

	protected SpagicClientInterrogaPGRemoteProxy(String serviceProxyUrl, String alfrescoUsername, String alfrescoPassword, String serviceUsername, String servicePassword, String restServiceUrl) {
		super(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	@Override
	public it.bologna.comune.spagic.interrogapg.Response cerca(String numeroPg, int annoPg, String codiceutente, Utente utente) throws SpagicClientException {
		if (codiceutente == null)
			throw new SpagicClientException(SpagicClientErrorCode.EINVALIDARGUMENT, "Codice utente non valorizzato");

		Request request = new Request();
		request.setAlfrescopassword(getAlfrescoPassword());
		request.setAlfrescousername(getAlfrescoUsername());
		request.setServicename(ServiceNamesUtil.INTERROGAPG);
		request.setUtente(SpagicClientRemoteProxyUtil.convert(utente));
		it.bologna.comune.spagic.interrogapg.Request requestInterrogaPG = new it.bologna.comune.spagic.interrogapg.Request();
		requestInterrogaPG.setNumeroProtocollo(numeroPg);
		requestInterrogaPG.setAnnoProtocollo(String.valueOf(annoPg));
		requestInterrogaPG.setCodiceUtenteEsterno(codiceutente);
		request.setInternalrequestparam(SpagicClientSerializationUtil.getRequestInterrogaPGToString(requestInterrogaPG));
		Response response = this.invokeSpagicService(request);
		if (response.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());

		it.bologna.comune.spagic.interrogapg.Response responseInterroga = SpagicClientSerializationUtil.getResponseInterrogaPGToObject(response.getResponseparam());
		if (!responseInterroga.getO9().getCodice().equalsIgnoreCase("0000"))
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);

		return responseInterroga;
	}

	@Override
	public it.bologna.comune.spagic.interrogapg.Response cercaDocumentale(String numeroPg, int annoPg, Utente utente) throws SpagicClientException {
		Request request = new Request();
		request.setAlfrescopassword(getAlfrescoPassword());
		request.setAlfrescousername(getAlfrescoUsername());
		request.setServicename(ServiceNamesUtil.INTERROGAPG);
		request.setUtente(SpagicClientRemoteProxyUtil.convert(utente));
		it.bologna.comune.spagic.interrogapg.Request requestInterrogaPG = new it.bologna.comune.spagic.interrogapg.Request();
		requestInterrogaPG.setNumeroProtocollo(numeroPg);
		requestInterrogaPG.setAnnoProtocollo(String.valueOf(annoPg));
		request.setInternalrequestparam(SpagicClientSerializationUtil.getRequestInterrogaPGToString(requestInterrogaPG));
		Response response = this.invokeSpagicService(request);
		if (response.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());

		it.bologna.comune.spagic.interrogapg.Response responseInterroga = SpagicClientSerializationUtil.getResponseInterrogaPGToObject(response.getResponseparam());
		if (!responseInterroga.getO9().getCodice().equalsIgnoreCase("0000"))
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);

		return responseInterroga;
	}

	@Override
	protected String getSpagicServiceId() {
		return ServiceNamesUtil.CONSOLE_PEC_DISPATCHER;
	}

	@Override
	protected String getJaxbRequestToXml(Request request) throws JAXBException {
		return SpagicClientSerializationUtil.getRequestToString(request);
	}

	@Override
	protected Response getXmlResponseToJaxb(String response) throws JAXBException {
		return SpagicClientSerializationUtil.getResponseXmlToObject(response);
	}
}
