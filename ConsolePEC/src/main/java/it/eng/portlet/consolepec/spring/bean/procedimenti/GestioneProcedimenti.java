package it.eng.portlet.consolepec.spring.bean.procedimenti;

import java.util.List;

import it.eng.cobo.consolepec.commons.procedimento.ChiusuraProcedimentoInput;
import it.eng.cobo.consolepec.commons.procedimento.ChiusuraProcedimentoOutput;
import it.eng.cobo.consolepec.commons.procedimento.PropostaChiusuraProcedimentoInput;
import it.eng.cobo.consolepec.commons.procedimento.PropostaChiusuraProcedimentoOutput;
import it.eng.consolepec.spagicclient.bean.request.procedimenti.GestioneProcedimentoRequest;
import it.eng.consolepec.spagicclient.bean.request.procedimenti.IterProcedimentoRequest;
import it.eng.consolepec.spagicclient.bean.response.procedimenti.GestioneProcedimentoResponse;
import it.eng.consolepec.spagicclient.bean.response.procedimenti.IterProcedimentoResponse;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

public interface GestioneProcedimenti {

	public GestioneProcedimentoResponse avviaProcedimento(GestioneProcedimentoRequest bean, String praticaPath, List<String> indirizziMail) throws SpagicClientException;

	public GestioneProcedimentoResponse avviaProcedimento(GestioneProcedimentoRequest bean, String praticaPath) throws SpagicClientException;

	@Deprecated
	public IterProcedimentoResponse getIterProcedimento(IterProcedimentoRequest bean) throws SpagicClientException;

	public ChiusuraProcedimentoOutput chiudiProcedimento(ChiusuraProcedimentoInput request, String idDocumentaleFascicolo);

	public PropostaChiusuraProcedimentoOutput propostaChiusuraProcedimento(PropostaChiusuraProcedimentoInput input);

}
