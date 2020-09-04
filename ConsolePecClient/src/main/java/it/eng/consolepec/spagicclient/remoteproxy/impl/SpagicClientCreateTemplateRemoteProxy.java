package it.eng.consolepec.spagicclient.remoteproxy.impl;

import javax.xml.bind.JAXBException;

import it.bologna.comune.alfresco.creazione.template.CampoMetadato;
import it.bologna.comune.alfresco.generic.service.Request;
import it.bologna.comune.alfresco.generic.service.Response;
import it.eng.consolepec.spagicclient.SpagicClientCreateTemplate;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.bean.request.template.CampoTemplate;
import it.eng.consolepec.spagicclient.bean.request.template.CreaTemplateEmailRequest;
import it.eng.consolepec.spagicclient.bean.request.template.CreaTemplatePdfRequest;
import it.eng.consolepec.spagicclient.bean.request.template.CreaTemplatePerCopia;
import it.eng.consolepec.spagicclient.remoteproxy.abs.AbstractSpagicClientRemoteProxy;
import it.eng.consolepec.spagicclient.remoteproxy.abs.NewSpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientRemoteProxyUtil;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class SpagicClientCreateTemplateRemoteProxy extends AbstractSpagicClientRemoteProxy<Request, Response> implements SpagicClientCreateTemplate {

	protected SpagicClientCreateTemplateRemoteProxy(String serviceProxyUrl, String alfrescoUsername, String alfrescoPassword, String serviceUsername, String servicePassword, String restServiceUrl) {
		super(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
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

	@Override
	public LockedPratica createTemplate(CreaTemplateEmailRequest trequest, Utente utente) throws SpagicClientException {
		Request request = new Request();
		request.setAlfrescopassword(getAlfrescoPassword());
		request.setAlfrescousername(getAlfrescoUsername());
		request.setServicename(ServiceNamesUtil.CREATE_TEMPLATE);
		request.setUtente(SpagicClientRemoteProxyUtil.convert(utente));

		it.bologna.comune.alfresco.creazione.template.RequestEmail createRequest = new it.bologna.comune.alfresco.creazione.template.RequestEmail();
		createRequest.setUtente(utente.getFullName());
		createRequest.setRuolo(trequest.getAssegnatario());
		createRequest.setUsername(utente.getUsername());
		createRequest.setDestination(trequest.getDestination());

		createRequest.setNome(trequest.getNome());
		createRequest.setDescrizione(trequest.getDescrizione());
		createRequest.setOggettoMail(trequest.getOggettoMail());
		createRequest.setCorpoMail(trequest.getCorpoMail());
		createRequest.setMittente(trequest.getMittente());

		createRequest.getDestinatario().addAll(trequest.getDestinatari());

		createRequest.getDestinatarioCC().addAll(trequest.getDestinatariCC());

		for (CampoTemplate campo : trequest.getCampi()) {

			it.bologna.comune.alfresco.creazione.template.CampoTemplate rqCampo = new it.bologna.comune.alfresco.creazione.template.CampoTemplate();

			rqCampo.setNome(campo.getNome());
			rqCampo.setTipo(campo.getTipo().name());
			rqCampo.setFormato(campo.getFormato());
			rqCampo.setRegexValidazione(campo.getRegexValidazione());
			rqCampo.setLunghezzaMassima(campo.getLunghezzaMassima());
			rqCampo.getValoriLista().addAll(campo.getValoriLista());

			if (campo.getCampoMetadato() != null) {
				CampoMetadato campoMetadato = new CampoMetadato();
				campoMetadato.setIdMetadato(campo.getCampoMetadato().getIdMetadato());
				campoMetadato.setEtichettaMetadato(campo.getCampoMetadato().getEtichettaMetadato());
				rqCampo.setCampoMetadato(campoMetadato);
			}

			createRequest.getCampo().add(rqCampo);
		}

		createRequest.getFascicoliAbilitati().addAll(trequest.getFascicoliAbilitati());

		try {
			request.setInternalrequestparam(NewSpagicClientSerializationUtil.serialize(createRequest));
		} catch (Exception e) {
			logger.error("", e);
		}

		Response response = invokeSpagicService(request);
		if (response.getError() != null) {
			throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());
		}
		return new LockedPratica(response.getResponseparam(), response.getApplicationxml());
	}

	@Override
	public LockedPratica createTemplate(CreaTemplatePdfRequest trequest, Utente utente) throws SpagicClientException {
		Request request = new Request();
		request.setAlfrescopassword(getAlfrescoPassword());
		request.setAlfrescousername(getAlfrescoUsername());
		request.setServicename(ServiceNamesUtil.CREATE_TEMPLATE_PDF);
		request.setUtente(SpagicClientRemoteProxyUtil.convert(utente));

		it.bologna.comune.alfresco.creazione.template.RequestPdf createRequest = new it.bologna.comune.alfresco.creazione.template.RequestPdf();
		createRequest.setUtente(utente.getFullName());
		createRequest.setRuolo(trequest.getAssegnatario());
		createRequest.setUsername(utente.getUsername());
		createRequest.setDestination(trequest.getDestination());

		createRequest.setTitoloFile(trequest.getTitoloFile());
		createRequest.setNome(trequest.getNome());
		createRequest.setDescrizione(trequest.getDescrizione());

		for (CampoTemplate campo : trequest.getCampi()) {

			it.bologna.comune.alfresco.creazione.template.CampoTemplate rqCampo = new it.bologna.comune.alfresco.creazione.template.CampoTemplate();

			rqCampo.setNome(campo.getNome());
			rqCampo.setTipo(campo.getTipo().name());
			rqCampo.setFormato(campo.getFormato());
			rqCampo.setRegexValidazione(campo.getRegexValidazione());
			rqCampo.setLunghezzaMassima(campo.getLunghezzaMassima());
			rqCampo.getValoriLista().addAll(campo.getValoriLista());

			if (campo.getCampoMetadato() != null) {
				CampoMetadato campoMetadato = new CampoMetadato();
				campoMetadato.setIdMetadato(campo.getCampoMetadato().getIdMetadato());
				campoMetadato.setEtichettaMetadato(campo.getCampoMetadato().getEtichettaMetadato());
				rqCampo.setCampoMetadato(campoMetadato);
			}

			createRequest.getCampo().add(rqCampo);
		}

		createRequest.getFascicoliAbilitati().addAll(trequest.getFascicoliAbilitati());

		try {
			request.setInternalrequestparam(NewSpagicClientSerializationUtil.serialize(createRequest));
		} catch (Exception e) {
			logger.error("", e);
		}

		Response response = invokeSpagicService(request);
		if (response.getError() != null) {
			throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());
		}
		return new LockedPratica(response.getResponseparam(), response.getApplicationxml());
	}

	@Override
	public LockedPratica createTemplatePerCopia(CreaTemplatePerCopia trequest, Utente utente) throws SpagicClientException {

		Request request = new Request();
		request.setAlfrescopassword(getAlfrescoPassword());
		request.setAlfrescousername(getAlfrescoUsername());
		request.setServicename(ServiceNamesUtil.CREATE_TEMPLATE_PER_COPIA);
		request.setUtente(SpagicClientRemoteProxyUtil.convert(utente));

		it.bologna.comune.alfresco.creazione.template.RequestCopiaTemplate createRequest = new it.bologna.comune.alfresco.creazione.template.RequestCopiaTemplate();
		createRequest.setIdDocumentale(trequest.getIdDocumentale());

		try {
			request.setInternalrequestparam(NewSpagicClientSerializationUtil.serialize(createRequest));
		} catch (Exception e) {
			logger.error("", e);
		}

		Response response = invokeSpagicService(request);
		if (response.getError() != null) {
			throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());
		}
		return new LockedPratica(response.getResponseparam(), response.getApplicationxml());
	}
}
