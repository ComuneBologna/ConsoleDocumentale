package it.eng.consolepec.spagicclient.remoteproxy.impl;

import javax.xml.bind.JAXBException;

import it.bologna.comune.alfresco.generic.service.Request;
import it.bologna.comune.alfresco.generic.service.Response;
import it.eng.consolepec.spagicclient.SpagicClientSganciaEmailDaFascicolo;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.abs.AbstractSpagicClientRemoteProxy;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientRemoteProxyUtil;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class SpagicClientSganciaEmailDaFascicoloRemoteProxy extends AbstractSpagicClientRemoteProxy<Request, Response> implements SpagicClientSganciaEmailDaFascicolo {

	protected SpagicClientSganciaEmailDaFascicoloRemoteProxy(String serviceProxyUrl, String alfrescoUsername, String alfrescoPassword, String serviceUsername, String servicePassword,
			String restServiceUrl) {
		super(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	@Override
	public LockedPratica sganciaEmail(String pathEmail, String pathFascicolo, Utente utente) throws SpagicClientException {
		Request genericRequest = new Request();
		genericRequest.setAlfrescopassword(getAlfrescoPassword());
		genericRequest.setAlfrescousername(getAlfrescoUsername());
		genericRequest.setServicename(ServiceNamesUtil.SGANCIA_MAIL);
		genericRequest.setUtente(SpagicClientRemoteProxyUtil.convert(utente));
		// Invoco il servizio di Alfresco che a sua volta invoce il task in cui sta la logica di rimozione della PecIn dal fascicolo.
		genericRequest.setRequestparam(pathEmail + "|" + pathFascicolo);

		Response response = invokeSpagicService(genericRequest);

		if (response.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());

		return new LockedPratica(response.getResponseparam(), response.getApplicationxml());
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
