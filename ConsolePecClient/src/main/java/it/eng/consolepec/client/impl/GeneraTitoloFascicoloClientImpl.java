package it.eng.consolepec.client.impl;

import java.math.BigInteger;

import it.eng.consolepec.client.AbstractConsolePecClient;
import it.eng.consolepec.client.ClientInvoker;
import it.eng.consolepec.client.GeneraTitoloFascicoloClient;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.bean.request.datiaggiuntivi.DatoAggiuntivo;
import it.eng.consolepec.spagicclient.bean.request.fascicolo.FascicoloRequest;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class GeneraTitoloFascicoloClientImpl extends AbstractConsolePecClient implements GeneraTitoloFascicoloClient {

	public GeneraTitoloFascicoloClientImpl(ClientInvoker clientInvoker) {
		super(clientInvoker);
	}

	@Override
	public String generaTitoloFascicoloDaTemplate(FascicoloRequest fascicoloRequest, Utente utente) throws SpagicClientException {

		it.bologna.comune.alfresco.creazione.fascicolo.Request createRequest = new it.bologna.comune.alfresco.creazione.fascicolo.Request();
		createRequest.setNote(fascicoloRequest.getNote());
		createRequest.setTitolo(fascicoloRequest.getTitolo());
		createRequest.setUtente(utente.getFullName());
		createRequest.setRuolo(fascicoloRequest.getAssegnatario());
		createRequest.setUsername(utente.getUsername());
		createRequest.setTipoFascicolo(fascicoloRequest.getTipo());

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

		return clientInvoker.invoke(ServiceNamesUtil.GENERA_TITOLO_FASCICOLO_DA_TEMPLATE, utente, createRequest);
	}

}
