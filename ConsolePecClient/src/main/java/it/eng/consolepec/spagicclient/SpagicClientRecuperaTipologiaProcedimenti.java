package it.eng.consolepec.spagicclient;

import it.bologna.comune.spagic.procedimenti.tipologie.Response;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

import java.util.Date;


public interface SpagicClientRecuperaTipologiaProcedimenti {
	
	public Response getTipologieProcedimenti(Date currentDate, Utente utente) throws SpagicClientException;

}
