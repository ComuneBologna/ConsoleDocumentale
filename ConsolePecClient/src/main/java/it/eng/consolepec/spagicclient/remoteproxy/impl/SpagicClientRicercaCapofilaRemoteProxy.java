package it.eng.consolepec.spagicclient.remoteproxy.impl;

import javax.xml.bind.JAXBException;

import it.bologna.comune.alfresco.generic.service.Request;
import it.bologna.comune.alfresco.generic.service.Response;
import it.eng.consolepec.spagicclient.SpagicClientRicercaCapofila;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.abs.AbstractSpagicClientRemoteProxy;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientRemoteProxyUtil;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientErrorCode;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.CapofilaDetail;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class SpagicClientRicercaCapofilaRemoteProxy extends AbstractSpagicClientRemoteProxy<Request, Response> implements SpagicClientRicercaCapofila {

	protected SpagicClientRicercaCapofilaRemoteProxy(String serviceProxyUrl, String alfrescoUsername, String alfrescoPassword, String serviceUsername, String servicePassword, String restServiceUrl) {
		super(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	@Override
	public CapofilaDetail cerca(String numeroPg, int annoPg, Utente utente) throws SpagicClientException {
		Request request = new Request();
		request.setAlfrescopassword(getAlfrescoPassword());
		request.setAlfrescousername(getAlfrescoUsername());
		request.setServicename(ServiceNamesUtil.SEARCHCAPOFILA);
		request.setUtente(SpagicClientRemoteProxyUtil.convert(utente));
		request.setInternalrequestparam(numeroPg + "|" + annoPg);
		Response response = this.invokeSpagicService(request);
		if (response.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());

		it.bologna.comune.spagic.ricercaba01.capofila.Response ricercaCapofilaResponse = SpagicClientSerializationUtil.getRicercaCapofilaResponseToObject(response.getResponseparam());

		if (!ricercaCapofilaResponse.getCodiceRitorno().equalsIgnoreCase("0000"))
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);

		return createResponse(ricercaCapofilaResponse, numeroPg, annoPg);
	}

	private CapofilaDetail createResponse(it.bologna.comune.spagic.ricercaba01.capofila.Response ricercaCapofilaResponse, String numeroPg, int annoPg) {

		CapofilaDetail c = new CapofilaDetail();

		String annoProtocollo = ricercaCapofilaResponse.getAnnoProtocollo();
		String numeroProtocollo = ricercaCapofilaResponse.getNumeroProtocollo();

		c.setCompleto(annoProtocollo != null && numeroProtocollo != null);

		if (annoProtocollo != null && numeroProtocollo != null) {
			/* controllo se si tratta di un capofila */
			boolean capofila = annoPg == Integer.parseInt(ricercaCapofilaResponse.getAnnoProtocollo()) && checkForNumeroPG(numeroPg, ricercaCapofilaResponse.getNumeroProtocollo());
			c.setCapofila(capofila);

			if (capofila) {
				c.setAnnoPg(annoPg);
				c.setNumeroPg(numeroPg);
				c.setAnnoPgCapofila(annoPg);
				c.setNumeroPgCapofila(numeroPg);
			} else {
				c.setAnnoPg(annoPg);
				c.setNumeroPg(numeroPg);
				c.setAnnoPgCapofila(Integer.parseInt(ricercaCapofilaResponse.getAnnoProtocollo()));
				c.setNumeroPgCapofila(ricercaCapofilaResponse.getNumeroProtocollo());
			}
		} else {
			c.setCapofila(false);
		}

		c.setTitolo(Integer.parseInt(ricercaCapofilaResponse.getCodTitolo()));
		c.setSezione(Integer.parseInt(ricercaCapofilaResponse.getCodSezione()));
		c.setRubrica(Integer.parseInt(ricercaCapofilaResponse.getCodRubrica()));
		c.setOggetto(ricercaCapofilaResponse.getOggetto());
		c.setDataProtocollazione(ricercaCapofilaResponse.getDataProtocollazione().toGregorianCalendar().getTime());
		c.setNumeroFascicolo(String.valueOf(ricercaCapofilaResponse.getNumeroFascicolo()));
		if (ricercaCapofilaResponse.getAnnoFascicolo() != null)
			c.setAnnoFascicolo(Integer.parseInt(ricercaCapofilaResponse.getAnnoFascicolo()));

		return c;
	}

	private boolean checkForNumeroPG(String numeroPg, String numeroProtocollo) {
		try {
			int num1 = Integer.parseInt(numeroProtocollo);
			int num2 = Integer.parseInt(numeroPg);
			return num1 == num2;
		} catch (NumberFormatException e) {
			return numeroPg.equalsIgnoreCase(numeroProtocollo);
		}
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
