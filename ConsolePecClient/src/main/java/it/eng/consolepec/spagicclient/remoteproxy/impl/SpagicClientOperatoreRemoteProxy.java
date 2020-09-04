package it.eng.consolepec.spagicclient.remoteproxy.impl;

import javax.xml.bind.JAXBException;

import it.bologna.comune.alfresco.generic.service.Request;
import it.bologna.comune.alfresco.generic.service.Response;
import it.eng.consolepec.spagicclient.SpagicClientOperatore;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.abs.AbstractSpagicClientRemoteProxy;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientRemoteProxyUtil;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class SpagicClientOperatoreRemoteProxy extends AbstractSpagicClientRemoteProxy<Request, Response> implements SpagicClientOperatore {

	protected SpagicClientOperatoreRemoteProxy(String serviceProxyUrl, String alfrescoUsername, String alfrescoPassword, String serviceUsername, String servicePassword, String restServiceUrl) {
		super(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
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

	@Override
	public LockedPratica modifica(String pathPratica, String operatore, Utente utente) throws SpagicClientException {
		Request genericRequest = new Request();
		genericRequest.setAlfrescopassword(getAlfrescoPassword());
		genericRequest.setAlfrescousername(getAlfrescoUsername());
		genericRequest.setServicename(ServiceNamesUtil.MODIFICA_OPERATORE);
		genericRequest.setUtente(SpagicClientRemoteProxyUtil.convert(utente));
		genericRequest.setPathapplicationxml(pathPratica);
		genericRequest.setRequestparam(operatore);

		Response response = invokeSpagicService(genericRequest);
		if (response.getError() != null) throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());
		return new LockedPratica(response.getResponseparam(), response.getApplicationxml());
	}

}
