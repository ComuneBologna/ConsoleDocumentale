package it.eng.consolepec.spagicclient;

import it.eng.consolepec.spagicclient.bean.request.modulistica.PraticaModulisticaRequest;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

public interface SpagicClientCreatePraticaModulistica {

	/**
	 * 
	 * @param request
	 * @param utente
	 * @return
	 * @throws SpagicClientException
	 */
	public LockedPratica createPraticaModulistica(PraticaModulisticaRequest request, Utente utente) throws SpagicClientException;

}
