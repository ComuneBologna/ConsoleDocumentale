package it.eng.consolepec.spagicclient.remoteproxy.impl;

import javax.xml.bind.JAXBException;

import it.bologna.comune.alfresco.creazione.template.CampoMetadato;
import it.bologna.comune.alfresco.generic.service.Request;
import it.bologna.comune.alfresco.generic.service.Response;
import it.eng.consolepec.spagicclient.SpagicClientSaveTemplate;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.bean.request.template.CampoTemplate;
import it.eng.consolepec.spagicclient.bean.request.template.CreaTemplateEmailRequest;
import it.eng.consolepec.spagicclient.bean.request.template.CreaTemplatePdfRequest;
import it.eng.consolepec.spagicclient.remoteproxy.abs.AbstractSpagicClientRemoteProxy;
import it.eng.consolepec.spagicclient.remoteproxy.abs.NewSpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientRemoteProxyUtil;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

/**
 *
 * @author biagiot
 *
 */
public class SpagicClientSaveTemplateRemoteProxy extends AbstractSpagicClientRemoteProxy<Request, Response> implements SpagicClientSaveTemplate {

	protected SpagicClientSaveTemplateRemoteProxy(String serviceProxyUrl, String alfrescoUsername, String alfrescoPassword, String serviceUsername, String servicePassword, String restServiceUrl) {
		super(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	@Override
	public LockedPratica saveTemplateMail(String pathTemplate, CreaTemplateEmailRequest trequest, Utente utente) throws SpagicClientException {
		Request request = new Request();
		request.setAlfrescopassword(getAlfrescoPassword());
		request.setAlfrescousername(getAlfrescoUsername());
		request.setServicename(ServiceNamesUtil.SAVE_TEMPLATE_MAIL);
		request.setPathapplicationxml(pathTemplate);
		request.setUtente(SpagicClientRemoteProxyUtil.convert(utente));

		it.bologna.comune.alfresco.creazione.template.RequestEmail updateRequest = new it.bologna.comune.alfresco.creazione.template.RequestEmail();
		updateRequest.setUtente(utente.getFullName());
		updateRequest.setRuolo(trequest.getAssegnatario());
		updateRequest.setUsername(utente.getUsername());
		updateRequest.setDestination(trequest.getDestination());

		updateRequest.setNome(trequest.getNome());
		updateRequest.setDescrizione(trequest.getDescrizione());
		updateRequest.setOggettoMail(trequest.getOggettoMail());
		updateRequest.setCorpoMail(trequest.getCorpoMail());
		updateRequest.setMittente(trequest.getMittente());
		updateRequest.getDestinatario().addAll(trequest.getDestinatari());
		updateRequest.getDestinatarioCC().addAll(trequest.getDestinatariCC());

		for (CampoTemplate campo : trequest.getCampi()) {
			it.bologna.comune.alfresco.creazione.template.CampoTemplate rqCampo = new it.bologna.comune.alfresco.creazione.template.CampoTemplate();
			rqCampo.setNome(campo.getNome());
			rqCampo.setTipo(campo.getTipo().name());
			rqCampo.setFormato(campo.getFormato());
			rqCampo.setRegexValidazione(campo.getRegexValidazione());
			rqCampo.setLunghezzaMassima(campo.getLunghezzaMassima());
			rqCampo.getValoriLista().addAll(campo.getValoriLista());

			if (campo.getCampoMetadato() != null) {
				CampoMetadato cm = new CampoMetadato();
				cm.setIdMetadato(campo.getCampoMetadato().getIdMetadato());
				cm.setEtichettaMetadato(campo.getCampoMetadato().getEtichettaMetadato());
				rqCampo.setCampoMetadato(cm);
			}

			updateRequest.getCampo().add(rqCampo);
		}

		updateRequest.getFascicoliAbilitati().addAll(trequest.getFascicoliAbilitati());

		try {
			request.setInternalrequestparam(NewSpagicClientSerializationUtil.serialize(updateRequest));

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
	public LockedPratica saveTemplatePDF(String pathTemplate, CreaTemplatePdfRequest trequest, Utente utente) throws SpagicClientException {
		Request request = new Request();
		request.setAlfrescopassword(getAlfrescoPassword());
		request.setAlfrescousername(getAlfrescoUsername());
		request.setServicename(ServiceNamesUtil.SAVE_TEMPLATE_PDF);
		request.setPathapplicationxml(pathTemplate);
		request.setUtente(SpagicClientRemoteProxyUtil.convert(utente));

		it.bologna.comune.alfresco.creazione.template.RequestPdf createRequest = new it.bologna.comune.alfresco.creazione.template.RequestPdf();
		createRequest.setUtente(utente.getFullName());
		createRequest.setRuolo(trequest.getAssegnatario());
		createRequest.setUsername(utente.getUsername());
		createRequest.setDestination(trequest.getDestination());

		createRequest.setNome(trequest.getNome());
		createRequest.setDescrizione(trequest.getDescrizione());
		createRequest.setTitoloFile(trequest.getTitoloFile());

		for (CampoTemplate campo : trequest.getCampi()) {

			it.bologna.comune.alfresco.creazione.template.CampoTemplate rqCampo = new it.bologna.comune.alfresco.creazione.template.CampoTemplate();

			rqCampo.setNome(campo.getNome());
			rqCampo.setTipo(campo.getTipo().name());
			rqCampo.setFormato(campo.getFormato());
			rqCampo.setRegexValidazione(campo.getRegexValidazione());
			rqCampo.setLunghezzaMassima(campo.getLunghezzaMassima());
			rqCampo.getValoriLista().addAll(campo.getValoriLista());

			if (campo.getCampoMetadato() != null) {
				CampoMetadato cm = new CampoMetadato();
				cm.setIdMetadato(campo.getCampoMetadato().getIdMetadato());
				cm.setEtichettaMetadato(campo.getCampoMetadato().getEtichettaMetadato());
				rqCampo.setCampoMetadato(cm);
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
