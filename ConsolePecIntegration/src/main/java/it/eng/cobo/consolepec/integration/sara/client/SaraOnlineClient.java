package it.eng.cobo.consolepec.integration.sara.client;

import it.eng.cobo.consolepec.integration.sara.client.emissionecontrassegno.EmissioneContrassegnoRequest;
import it.eng.cobo.consolepec.integration.sara.client.emissionecontrassegno.EmissioneContrassegnoResponse;
import it.eng.cobo.consolepec.integration.sara.client.emissionepermesso.EmissionePermessoRequest;
import it.eng.cobo.consolepec.integration.sara.client.emissionepermesso.EmissionePermessoResponse;




/**
 * 
 * @author biagiot
 *
 */
public interface SaraOnlineClient {

	EmissionePermessoResponse emissionePermesso(EmissionePermessoRequest request);
	
	EmissioneContrassegnoResponse emissioneContrassegno(EmissioneContrassegnoRequest request);
}
