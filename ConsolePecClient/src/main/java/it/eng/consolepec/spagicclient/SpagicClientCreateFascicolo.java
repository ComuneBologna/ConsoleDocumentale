package it.eng.consolepec.spagicclient;

import it.eng.consolepec.spagicclient.bean.request.fascicolo.FascicoloRequest;
import it.eng.consolepec.spagicclient.bean.request.fascicolo.GenericFascicoloRequest;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

public interface SpagicClientCreateFascicolo {

	/**
	 * 
	 * @param fascicoloRequest
	 * @param utente
	 * @return
	 * @throws SpagicClientException
	 * 
	 * @Deprecated Utilizzare il metodo {@link createFascicolo(FascicoloRequest fascicoloRequest, Utente utente)}
	 * 
	 */
	@Deprecated
	public LockedPratica createFascicolo(GenericFascicoloRequest fascicoloRequest, Utente utente) throws SpagicClientException;

	/**
	 * 
	 * @param fascicoloRequest
	 * @param utente
	 * @return
	 * @throws SpagicClientException
	 */
	public LockedPratica createFascicolo(FascicoloRequest fascicoloRequest, Utente utente) throws SpagicClientException;

}
