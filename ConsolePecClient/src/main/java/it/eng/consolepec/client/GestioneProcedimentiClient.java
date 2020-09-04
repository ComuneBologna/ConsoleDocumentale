package it.eng.consolepec.client;

import it.eng.cobo.consolepec.commons.procedimento.ChiusuraProcedimentoInput;
import it.eng.cobo.consolepec.commons.procedimento.ChiusuraProcedimentoOutput;
import it.eng.cobo.consolepec.commons.procedimento.PropostaChiusuraProcedimentoInput;
import it.eng.cobo.consolepec.commons.procedimento.PropostaChiusuraProcedimentoOutput;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

public interface GestioneProcedimentiClient {

	ChiusuraProcedimentoOutput chiudiProcedimento(ChiusuraProcedimentoInput input, String idDocumentale, Utente utente) throws SpagicClientException;

	PropostaChiusuraProcedimentoOutput propostaChiusuraProcedimento(PropostaChiusuraProcedimentoInput input, Utente utente) throws SpagicClientException;
}
