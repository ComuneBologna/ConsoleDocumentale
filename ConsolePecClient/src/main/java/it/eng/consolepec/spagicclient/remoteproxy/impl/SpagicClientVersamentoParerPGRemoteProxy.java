package it.eng.consolepec.spagicclient.remoteproxy.impl;

import javax.xml.bind.JAXBException;

import it.bologna.comune.alfresco.generic.service.Request;
import it.bologna.comune.alfresco.generic.service.Response;
import it.eng.consolepec.spagicclient.SpagicClientVersamentoParerPG;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.abs.AbstractSpagicClientRemoteProxy;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientRemoteProxyUtil;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientErrorCode;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class SpagicClientVersamentoParerPGRemoteProxy extends AbstractSpagicClientRemoteProxy<Request, Response> implements SpagicClientVersamentoParerPG {

	protected SpagicClientVersamentoParerPGRemoteProxy(String serviceProxyUrl, String alfrescoUsername, String alfrescoPassword, String serviceUsername, String servicePassword,
			String restServiceUrl) {
		super(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	@Override
	public it.bologna.comune.spagic.versamentoparerpg.Response inserimento(String numeroPg, int annoPg, String chiaveUD, Utente utente) throws SpagicClientException {
		Request request = new Request();
		request.setAlfrescopassword(getAlfrescoPassword());
		request.setAlfrescousername(getAlfrescoUsername());
		request.setServicename(ServiceNamesUtil.VERSAMENTOPARERPG);
		request.setUtente(SpagicClientRemoteProxyUtil.convert(utente));
		it.bologna.comune.spagic.versamentoparerpg.Request requestVersamento = new it.bologna.comune.spagic.versamentoparerpg.Request();
		requestVersamento.setNumeroProtocollo(numeroPg);
		requestVersamento.setAnnoProtocollo(String.valueOf(annoPg));
		requestVersamento.setTipoOperazione("I");
		requestVersamento.setChiaveUd(chiaveUD);
		request.setInternalrequestparam(SpagicClientSerializationUtil.getRequestVersamentoParerPGToString(requestVersamento));
		Response response = this.invokeSpagicService(request);
		if (response.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());

		it.bologna.comune.spagic.versamentoparerpg.Response responseVersamento = SpagicClientSerializationUtil.getResponseVersamentoParerPGToObject(response.getResponseparam());
		if (!responseVersamento.getCodice().equals("0000"))
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);

		return responseVersamento;
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
