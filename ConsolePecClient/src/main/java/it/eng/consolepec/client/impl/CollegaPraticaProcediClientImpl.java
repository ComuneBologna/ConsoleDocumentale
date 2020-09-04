package it.eng.consolepec.client.impl;

import java.util.List;

import it.eng.cobo.consolepec.commons.urbanistica.PraticaProcedi;
import it.eng.consolepec.client.AbstractConsolePecClient;
import it.eng.consolepec.client.ClientInvoker;
import it.eng.consolepec.client.CollegaPraticaProcediClient;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

/**
 * @author GiacomoFM
 * @since 08/nov/2017
 */
public class CollegaPraticaProcediClientImpl extends AbstractConsolePecClient implements CollegaPraticaProcediClient {

	public CollegaPraticaProcediClientImpl(ClientInvoker clientInvoker) {
		super(clientInvoker);
	}

	@Override
	public LockedPratica collega(String pathFascicolo, PraticaProcedi praticaProcedi, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.COLLEGA_PRATICA_PROCEDI, utente, pathFascicolo, praticaProcedi);
	}

	@Override
	public LockedPratica eliminaCollega(String pathFascicolo, List<PraticaProcedi> praticheProcedi, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.ELIMINA_COLLEGA_PRATICA_PROCEDI, utente, pathFascicolo, praticheProcedi);
	}

}
