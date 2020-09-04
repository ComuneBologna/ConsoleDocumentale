package it.eng.consolepec.spagicclient.remoteproxy.impl;

import java.io.Serializable;

import javax.xml.bind.JAXBException;

import it.bologna.comune.alfresco.creazione.modulistica.Request.Modulistica;
import it.bologna.comune.alfresco.creazione.modulistica.Valore;
import it.bologna.comune.alfresco.creazione.modulistica.Valore.Tabella;
import it.bologna.comune.alfresco.generic.service.Request;
import it.bologna.comune.alfresco.generic.service.Response;
import it.eng.consolepec.spagicclient.SpagicClientCreatePraticaModulistica;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.bean.request.modulistica.NodoModulistica;
import it.eng.consolepec.spagicclient.bean.request.modulistica.NodoModulistica.TipoNodoModulistica;
import it.eng.consolepec.spagicclient.bean.request.modulistica.PraticaModulisticaRequest;
import it.eng.consolepec.spagicclient.bean.request.modulistica.Sezione;
import it.eng.consolepec.spagicclient.bean.request.modulistica.ValoreModulo;
import it.eng.consolepec.spagicclient.bean.request.modulistica.ValoreModulo.TabellaModulo.Riga;
import it.eng.consolepec.spagicclient.remoteproxy.abs.AbstractSpagicClientRemoteProxy;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientRemoteProxyUtil;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class SpagicClientCreatePraticaModulisticaRemoteProxy extends AbstractSpagicClientRemoteProxy<Request, Response> implements SpagicClientCreatePraticaModulistica {

	protected SpagicClientCreatePraticaModulisticaRemoteProxy(String serviceProxyUrl, String alfrescoUsername, String alfrescoPassword, String serviceUsername, String servicePassword,
			String restServiceUrl) {
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
	public LockedPratica createPraticaModulistica(PraticaModulisticaRequest pmrequest, Utente utente) throws SpagicClientException {
		Request request = new Request();
		request.setAlfrescopassword(getAlfrescoPassword());
		request.setAlfrescousername(getAlfrescoUsername());
		request.setServicename(ServiceNamesUtil.CREATEPRATICAMODULISTICA);
		request.setUtente(SpagicClientRemoteProxyUtil.convert(utente));

		it.bologna.comune.alfresco.creazione.modulistica.Request createRequest = new it.bologna.comune.alfresco.creazione.modulistica.Request();
		createRequest.setTitolo(pmrequest.getTitolo());
		createRequest.setUtente(utente.getFullName());
		createRequest.setRuolo(pmrequest.getAssegnatario());
		createRequest.setUsername(utente.getUsername());
		createRequest.setDestination(pmrequest.getDestination());
		createRequest.setModulistica(new Modulistica());
		createRequest.getModulistica().setNome(pmrequest.getNome());

		for (NodoModulistica nodo : pmrequest.getNodi()) {
			createRequest.getModulistica().getValoriOrSezione().add(createNodo(nodo));
		}

		try {
			request.setInternalrequestparam(SpagicClientSerializationUtil.getCreatePraticaModulisticaRequestToString(createRequest));
		} catch (Exception e) {
			logger.error("", e);
		}
		Response response = invokeSpagicService(request);
		if (response.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());
		return new LockedPratica(response.getResponseparam(), response.getApplicationxml());
	}

	private Valore creaValore(ValoreModulo v) {
		Valore valore = new Valore();
		valore.setNome(v.getNome());
		valore.setValore(v.getValore());
		valore.setTipo(v.getTipo().name());
		valore.setEtichetta(v.getEtichetta());
		valore.setDescrizione(v.getDescrizione());
		valore.setVisibile(v.isVisibile());

		Tabella tabella = null;
		if (v.getTabella() != null) {
			tabella = new Tabella();
			for (Riga riga : v.getTabella().getRighe()) {
				it.bologna.comune.alfresco.creazione.modulistica.Valore.Tabella.Riga r = new it.bologna.comune.alfresco.creazione.modulistica.Valore.Tabella.Riga();
				for (ValoreModulo colonna : riga.getColonne()) {
					r.getColonne().add(creaValore(colonna));
				}
				tabella.getRiga().add(r);
			}
		}
		valore.setTabella(tabella);
		return valore;
	}

	private Serializable createNodo(NodoModulistica nodo) {
		if (nodo.getTipoNodo() == TipoNodoModulistica.VALORE_MODULO) {
			return creaValore((ValoreModulo) nodo);
		}
		if (nodo.getTipoNodo() == TipoNodoModulistica.SEZIONE) {
			Sezione sezione = (Sezione) nodo;
			it.bologna.comune.alfresco.creazione.modulistica.Sezione s = new it.bologna.comune.alfresco.creazione.modulistica.Sezione();
			s.setTitolo(sezione.getTitolo());
			for (NodoModulistica nodoSezione : sezione.getNodi()) {
				s.getSezioneOrValori().add(createNodo(nodoSezione));
			}
			return s;
		}
		return null;
	}

}
