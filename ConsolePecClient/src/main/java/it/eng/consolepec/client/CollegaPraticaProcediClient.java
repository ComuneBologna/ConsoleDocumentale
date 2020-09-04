package it.eng.consolepec.client;

import java.util.List;

import it.eng.cobo.consolepec.commons.urbanistica.PraticaProcedi;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

/**
 * @author GiacomoFM
 * @since 08/nov/2017
 */
public interface CollegaPraticaProcediClient {

	LockedPratica collega(String pathFascicolo, PraticaProcedi praticaProcedi, Utente utente) throws SpagicClientException;

	LockedPratica eliminaCollega(String pathFascicolo, List<PraticaProcedi> praticheProcedi, Utente utente) throws SpagicClientException;
}
