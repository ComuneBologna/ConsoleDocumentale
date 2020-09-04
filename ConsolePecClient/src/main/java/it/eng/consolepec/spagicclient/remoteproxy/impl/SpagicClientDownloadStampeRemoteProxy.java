/**
 * 
 */
package it.eng.consolepec.spagicclient.remoteproxy.impl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import it.bologna.comune.alfresco.generic.service.Request;
import it.bologna.comune.alfresco.generic.service.Response;
import it.bologna.comune.spagic.stampe.ChiaviStampa;
import it.bologna.comune.spagic.stampe.ParametroStampa;
import it.bologna.comune.spagic.stampe.TipoStampa;
import it.eng.consolepec.spagicclient.SpagicClientDownloadStampe;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.ResponseWithAttachementsDto;
import it.eng.consolepec.spagicclient.remoteproxy.abs.AbstractSpagicClientRemoteProxy;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientRemoteProxyUtil;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

/**
 * @author Filippo
 * 
 */
public class SpagicClientDownloadStampeRemoteProxy extends AbstractSpagicClientRemoteProxy<Request, Response> implements SpagicClientDownloadStampe {

	protected SpagicClientDownloadStampeRemoteProxy(String serviceProxyUrl, String alfrescoUsername, String alfrescoPassword, String serviceUsername, String servicePassword, String restServiceUrl) {
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
	public ResponseWithAttachementsDto<Response> downloadRiversamentoCartaceo(String praticaPath, String numPG, String annoPG, Utente utente) throws SpagicClientException {

		it.bologna.comune.spagic.stampe.Request rivReq = new it.bologna.comune.spagic.stampe.Request();
		rivReq.setTipoStampa(TipoStampa.RIVERSAMENTO_CARTACEO);
		List<ParametroStampa> params = new ArrayList<ParametroStampa>();

		ParametroStampa paramPraticaPath = new ParametroStampa();
		paramPraticaPath.setChiave(ChiaviStampa.PRATICA_PATH.value());
		paramPraticaPath.setValore(praticaPath);
		params.add(paramPraticaPath);

		ParametroStampa paramNumPG = new ParametroStampa();
		paramNumPG.setChiave(ChiaviStampa.NUM_PG.value());
		paramNumPG.setValore(numPG);
		params.add(paramNumPG);

		ParametroStampa paramAnnoPG = new ParametroStampa();
		paramAnnoPG.setChiave(ChiaviStampa.ANNO_PG.value());
		paramAnnoPG.setValore(annoPG);
		params.add(paramAnnoPG);

		rivReq.getParametriStampa().addAll(params);

		return downloadStampa(rivReq, utente);
	}

	@Override
	public ResponseWithAttachementsDto<Response> downloadRicevuteDiConsegna(String praticaPath, Utente utente) throws SpagicClientException {

		it.bologna.comune.spagic.stampe.Request ricReq = new it.bologna.comune.spagic.stampe.Request();
		ricReq.setTipoStampa(TipoStampa.RICEVUTA_CONSEGNA);
		List<ParametroStampa> params = new ArrayList<ParametroStampa>();

		ParametroStampa paramPraticaPath = new ParametroStampa();
		paramPraticaPath.setChiave(ChiaviStampa.PRATICA_PATH.value());
		paramPraticaPath.setValore(praticaPath);
		params.add(paramPraticaPath);

		ricReq.getParametriStampa().addAll(params);

		return downloadStampa(ricReq, utente);
	}

	private ResponseWithAttachementsDto<Response> downloadStampa(it.bologna.comune.spagic.stampe.Request internalRequest, Utente utente) {
		Request request = new Request();
		request.setAlfrescopassword(getAlfrescoPassword());
		request.setAlfrescousername(getAlfrescoUsername());
		request.setServicename(ServiceNamesUtil.DOWNLOAD_STAMPE);
		request.setUtente(SpagicClientRemoteProxyUtil.convert(utente));
		request.setInternalrequestparam(SpagicClientSerializationUtil.getRequestDownloadStampeToString(internalRequest));
		ResponseWithAttachementsDto<Response> response = invokeSpagicServiceWhitAttachementResponse(request);
		return response;
	}

}
