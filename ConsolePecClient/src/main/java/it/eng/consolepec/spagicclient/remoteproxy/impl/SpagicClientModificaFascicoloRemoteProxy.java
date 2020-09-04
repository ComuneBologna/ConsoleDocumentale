package it.eng.consolepec.spagicclient.remoteproxy.impl;

import java.util.List;

import javax.xml.bind.JAXBException;

import it.bologna.comune.alfresco.generic.service.Request;
import it.bologna.comune.alfresco.generic.service.Response;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.consolepec.spagicclient.SpagicClientModificaFascicolo;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.abs.AbstractSpagicClientRemoteProxy;
import it.eng.consolepec.spagicclient.remoteproxy.abs.NewSpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientRemoteProxyUtil;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.spagicclient.remoteproxy.util.DatiAggiuntiviUtil;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

/**
 * @author GiacomoFM
 * @since 10/lug/2017
 */
public class SpagicClientModificaFascicoloRemoteProxy extends AbstractSpagicClientRemoteProxy<Request, Response> implements SpagicClientModificaFascicolo {

	protected SpagicClientModificaFascicoloRemoteProxy(String serviceProxyUrl, String alfrescoUsername, String alfrescoPassword, String serviceUsername, String servicePassword,
			String restServiceUrl) {
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
	public LockedPratica modifica(String pathFascicolo, String nuovoTitolo, String nuovoTipoFascicolo, List<DatoAggiuntivo> datoAggiuntivo, Utente utente) throws SpagicClientException {
		Request request = new Request();
		request.setAlfrescopassword(getAlfrescoPassword());
		request.setAlfrescousername(getAlfrescoUsername());
		request.setUtente(SpagicClientRemoteProxyUtil.convert(utente));
		request.setServicename(ServiceNamesUtil.MODIFICA_FASCICOLO);

		it.bologna.comune.spagic.modifica.fascicolo.Request modRequest = new it.bologna.comune.spagic.modifica.fascicolo.Request();
		modRequest.setPathapplicationxml(pathFascicolo);
		modRequest.setTitolo(nuovoTitolo);
		modRequest.setTipofascicolo(nuovoTipoFascicolo);

		for (DatoAggiuntivo dag : datoAggiuntivo) {
			modRequest.getDatoAggiuntivo().add(DatiAggiuntiviUtil.datoCommonToDatoBase(dag));
		}

		request.setInternalrequestparam(NewSpagicClientSerializationUtil.serialize(modRequest));

		Response response = invokeSpagicService(request);
		if (response.getError() != null) throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());
		return new LockedPratica(response.getResponseparam(), response.getApplicationxml());
	}

}
