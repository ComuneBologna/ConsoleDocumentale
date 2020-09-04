package it.eng.consolepec.client.impl;

import it.eng.cobo.consolepec.commons.procedimento.ChiusuraProcedimentoInput;
import it.eng.cobo.consolepec.commons.procedimento.ChiusuraProcedimentoOutput;
import it.eng.cobo.consolepec.commons.procedimento.PropostaChiusuraProcedimentoInput;
import it.eng.cobo.consolepec.commons.procedimento.PropostaChiusuraProcedimentoOutput;
import it.eng.consolepec.client.AbstractConsolePecClient;
import it.eng.consolepec.client.ClientInvoker;
import it.eng.consolepec.client.GestioneProcedimentiClient;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class GestioneProcedimentiClientImpl extends AbstractConsolePecClient implements GestioneProcedimentiClient {

	public GestioneProcedimentiClientImpl(ClientInvoker clientInvoker) {
		super(clientInvoker);
	}

	@Override
	public ChiusuraProcedimentoOutput chiudiProcedimento(ChiusuraProcedimentoInput input, String idDocumentale, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.CHIUDI_PROCEDIMENTI, utente, input, idDocumentale);
	}

	@Override
	public PropostaChiusuraProcedimentoOutput propostaChiusuraProcedimento(PropostaChiusuraProcedimentoInput input, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.PROPONI_CHIUSURA_PROCEDIMENTI, utente, input);
	}

}
