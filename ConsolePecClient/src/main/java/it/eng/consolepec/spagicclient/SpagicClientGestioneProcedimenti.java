package it.eng.consolepec.spagicclient;

import java.util.List;

import it.eng.consolepec.spagicclient.bean.request.procedimenti.GestioneProcedimentoRequest;
import it.eng.consolepec.spagicclient.bean.request.procedimenti.IterProcedimentoRequest;
import it.eng.consolepec.spagicclient.bean.response.procedimenti.GestioneProcedimentoResponse;
import it.eng.consolepec.spagicclient.bean.response.procedimenti.IterProcedimentoResponse;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

public interface SpagicClientGestioneProcedimenti {

	public GestioneProcedimentoResponse avvioProcedimento(GestioneProcedimentoRequest avvioProcedimento, String praticaPath, List<String> indirizziMail, Utente utente) throws SpagicClientException;

	@Deprecated
	public IterProcedimentoResponse getIterProcedimento(IterProcedimentoRequest iterRequest, Utente utente) throws SpagicClientException;

}
