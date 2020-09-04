package it.eng.consolepec.spagicclient.remoteproxy.impl;

import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBException;

import it.bologna.comune.alfresco.generic.service.Request;
import it.bologna.comune.alfresco.generic.service.Response;
import it.bologna.comune.spagic.gestione.template.Request.Valori;
import it.bologna.comune.spagic.gestione.template.TemplatePdfRequest;
import it.eng.consolepec.spagicclient.SpagicClientGestioneTemplate;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.abs.AbstractSpagicClientRemoteProxy;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientRemoteProxyUtil;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class SpagicClientGestioneTemplateRemoteProxy extends AbstractSpagicClientRemoteProxy<Request, Response> implements SpagicClientGestioneTemplate {

	protected SpagicClientGestioneTemplateRemoteProxy(String serviceProxyUrl, String alfrescoUsername, String alfrescoPassword, String serviceUsername, String servicePassword, String restServiceUrl) {
		super(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	@Override
	public boolean elimina(String pathTemplate, Utente utente) throws SpagicClientException {

		Request request = new Request();
		request.setAlfrescopassword(getAlfrescoPassword());
		request.setAlfrescousername(getAlfrescoUsername());
		request.setServicename(ServiceNamesUtil.ELIMINATEMPLATE);
		request.setPathapplicationxml(pathTemplate);
		request.setUtente(SpagicClientRemoteProxyUtil.convert(utente));

		Response response = invokeSpagicService(request);
		if (response.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());

		return true;
	}

	@Override
	public LockedPratica creaPDF(String pathFascicolo, String pathTemplate, Map<String, String> valori, String nomeFile, Utente utente) throws SpagicClientException {
		Request genericRequest = new Request();
		genericRequest.setAlfrescopassword(getAlfrescoPassword());
		genericRequest.setAlfrescousername(getAlfrescoUsername());
		genericRequest.setServicename(ServiceNamesUtil.TEMPLATE_PDF_OUTPUT_SERVICE);
		genericRequest.setUtente(SpagicClientRemoteProxyUtil.convert(utente));

		TemplatePdfRequest customRequest = new TemplatePdfRequest();
		customRequest.setPathFascicolo(pathFascicolo);
		customRequest.setPathTemplate(pathTemplate);
		customRequest.setFileName(nomeFile);
		for (Entry<String, String> entry : valori.entrySet()) {
			Valori v = new Valori();
			v.setChiave(entry.getKey());
			v.setValore(entry.getValue());
			customRequest.getValori().add(v);
		}

		genericRequest.setRequestparam(SpagicClientSerializationUtil.getRequestGestioneTemplatePDFToString(customRequest));
		Response genericResponse = invokeSpagicService(genericRequest);
		if (genericResponse.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(genericResponse.getError());

		return new LockedPratica(genericResponse.getResponseparam(), genericResponse.getApplicationxml());
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
