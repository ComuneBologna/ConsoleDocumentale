package it.eng.cobo.consolepec.integration.lag.client;

import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.cobo.consolepec.commons.exception.InvalidArgumentException;
import it.eng.cobo.consolepec.commons.exception.NotFoundException;
import it.eng.cobo.consolepec.integration.lag.bean.PersonaFisicaDto;

/**
 * @author GiacomoFM
 * @since 16/nov/2018
 */
public interface LagWsClient {

	/**
	 * Restituisce il dettaglio di una persona fisica, cercata per codice fiscale, null se non trovata
	 *
	 * @param codiceFiscale
	 * @throws InvalidArgumentException
	 * @throws ApplicationException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	PersonaFisicaDto dettaglioPersonaFisicaByCodiceFiscale(String codiceFiscale) throws InvalidArgumentException, ApplicationException, NotFoundException;

}
