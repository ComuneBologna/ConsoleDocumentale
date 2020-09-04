package it.eng.consolepec.spagicclient.remoteproxy.impl;

import java.math.BigInteger;

import javax.xml.bind.JAXBException;

import it.bologna.comune.alfresco.generic.service.Request;
import it.bologna.comune.alfresco.generic.service.Response;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.consolepec.spagicclient.SpagicClientCreateFascicolo;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.bean.request.datiaggiuntivi.DatoAggiuntivo;
import it.eng.consolepec.spagicclient.bean.request.fascicolo.FascicoloFatturazioneRequest;
import it.eng.consolepec.spagicclient.bean.request.fascicolo.FascicoloRequest;
import it.eng.consolepec.spagicclient.bean.request.fascicolo.GenericFascicoloRequest;
import it.eng.consolepec.spagicclient.remoteproxy.abs.AbstractSpagicClientRemoteProxy;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientRemoteProxyUtil;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.spagicclient.remoteproxy.util.DatiAggiuntiviUtil;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class SpagicClientCreateFascicoloRemoteProxy extends AbstractSpagicClientRemoteProxy<Request, Response> implements SpagicClientCreateFascicolo {

	protected SpagicClientCreateFascicoloRemoteProxy(String serviceProxyUrl, String alfrescoUsername, String alfrescoPassword, String serviceUsername, String servicePassword, String restServiceUrl) {
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
	public LockedPratica createFascicolo(GenericFascicoloRequest fascicoloRequest, Utente utente) throws SpagicClientException {
		Request request = new Request();
		request.setAlfrescopassword(getAlfrescoPassword());
		request.setAlfrescousername(getAlfrescoUsername());
		request.setPathapplicationxml(fascicoloRequest.getPathPraticaDaCollegare());
		request.setServicename(ServiceNamesUtil.CREATEFASCICOLO);
		request.setUtente(SpagicClientRemoteProxyUtil.convert(utente));

		it.bologna.comune.alfresco.creazione.fascicolo.Request createRequest = new it.bologna.comune.alfresco.creazione.fascicolo.Request();
		createRequest.setNote(fascicoloRequest.getNote());
		createRequest.setTitolo(fascicoloRequest.getTitolo());
		createRequest.setUtente(utente.getFullName());
		createRequest.setRuolo(fascicoloRequest.getAssegnatario());
		createRequest.setUsername(utente.getUsername());
		createRequest.setTipoFascicolo(fascicoloRequest.getTipo().name());
		if (TipologiaPratica.FASCICOLO_FATTURAZIONE_ELETTRONICA.getNomeTipologia().equals(fascicoloRequest.getTipo().name())) {
			FascicoloFatturazioneRequest fascicoloFatturazioneRequest = (FascicoloFatturazioneRequest) fascicoloRequest;
			createRequest.setNumeroFattura(fascicoloFatturazioneRequest.getNumeroFattura());
			createRequest.setRagioneSociale(fascicoloFatturazioneRequest.getRagioneSociale());
			createRequest.setPartitaIva(fascicoloFatturazioneRequest.getPIva());
			createRequest.setCodicePartitaIva(fascicoloFatturazioneRequest.getCodicePIva());
		}

		for (DatoAggiuntivo da : fascicoloRequest.getDatiAggiuntivi()) {
			it.bologna.comune.alfresco.creazione.fascicolo.Request.DatoAggiuntivo datoAggiuntivo = new it.bologna.comune.alfresco.creazione.fascicolo.Request.DatoAggiuntivo();
			datoAggiuntivo.setNome(da.getNome());
			datoAggiuntivo.setValore(da.getValore());
			datoAggiuntivo.setDescrizione(da.getDescrizione());
			datoAggiuntivo.setTipo(da.getTipo().name());
			datoAggiuntivo.setEditabile(da.getEditabile() != null ? da.getEditabile() : false);
			datoAggiuntivo.setObbligatorio(da.getObbligatorio() != null ? da.getObbligatorio() : false);
			if (da.getPosizione() != null) {
				datoAggiuntivo.setPosizione(new BigInteger(da.getPosizione().toString()));
			}
			datoAggiuntivo.setVisibile(da.getVisibile() != null ? da.getVisibile() : false);
			datoAggiuntivo.setIdAnagrafica(da.getIdAnagrafica());
			createRequest.getDatoAggiuntivo().add(datoAggiuntivo);
		}

		try {
			request.setInternalrequestparam(SpagicClientSerializationUtil.getCreateFascicoloRequestToString(createRequest));
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
	public LockedPratica createFascicolo(FascicoloRequest fascicoloRequest, Utente utente) throws SpagicClientException {
		Request request = new Request();
		request.setAlfrescopassword(getAlfrescoPassword());
		request.setAlfrescousername(getAlfrescoUsername());
		request.setPathapplicationxml(fascicoloRequest.getPathPraticaDaCollegare());
		request.setServicename(ServiceNamesUtil.CREATEFASCICOLO);
		request.setUtente(SpagicClientRemoteProxyUtil.convert(utente));

		it.bologna.comune.alfresco.creazione.fascicolo.Request createRequest = new it.bologna.comune.alfresco.creazione.fascicolo.Request();
		createRequest.setNote(fascicoloRequest.getNote());
		createRequest.setTitolo(fascicoloRequest.getTitolo());
		createRequest.setUtente(utente.getFullName());
		createRequest.setRuolo(fascicoloRequest.getAssegnatario());
		createRequest.setUsername(utente.getUsername());
		createRequest.setTipoFascicolo(fascicoloRequest.getTipo());

		if (TipologiaPratica.FASCICOLO_FATTURAZIONE_ELETTRONICA.getNomeTipologia().equals(fascicoloRequest.getTipo())) {
			createRequest.setNumeroFattura(fascicoloRequest.getNumeroFattura());
			createRequest.setRagioneSociale(fascicoloRequest.getRagioneSociale());
			createRequest.setPartitaIva(fascicoloRequest.getPIva());
			createRequest.setCodicePartitaIva(fascicoloRequest.getCodicePIva());
		}

		for (DatoAggiuntivo da : fascicoloRequest.getDatiAggiuntivi()) {
			createRequest.getDatoAggiuntivo().add(DatiAggiuntiviUtil.datoSpagicToDatoCreazione(da));
		}

		try {
			request.setInternalrequestparam(SpagicClientSerializationUtil.getCreateFascicoloRequestToString(createRequest));
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
