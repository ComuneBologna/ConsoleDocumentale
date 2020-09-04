package it.eng.cobo.consolepec.integration.sara.client;

import it.eng.cobo.consolepec.integration.sara.client.SaraWSProfilaturaRequest.GetRuoliProfiloResponse;

/**
 * Client per invocazione WS Profilatura SARA
 * 
 * @author biagiot
 *
 */
public interface SaraWSProfilaturaClient {

	GetRuoliProfiloResponse getRuoliProfilo(String codiceFiscale);

}
