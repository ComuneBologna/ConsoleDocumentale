package it.eng.cobo.consolepec.integration.sara.profilazione;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.eng.cobo.consolepec.integration.sara.client.SaraWSProfilaturaClient;
import it.eng.cobo.consolepec.integration.sara.client.SaraWSProfilaturaProperties;
import it.eng.cobo.consolepec.integration.sara.client.SaraWSProfilaturaRequest.GetRuoliProfiloResponse;
import it.eng.cobo.consolepec.integration.sara.client.impl.SaraWSProfilaturaClientImpl;

/**
 * 
 * @author biagiot
 *
 */
public class SaraWSProfilaturaClientTest {

	private SaraWSProfilaturaClient saraWSProfilaturaClient;
	private final String codiceFiscale = "frglsn87m02a944i";

	@Before
	public void before() {
		SaraWSProfilaturaProperties properties = new SaraWSProfilaturaProperties();
		properties.getProperties().put("endpoint", "http://sara.test.comune.bologna.it/WSProfilatura/services/wsProfilatura");
		properties.getProperties().put("matricola", "profilatura");
		properties.getProperties().put("password", "profilatura");
		this.saraWSProfilaturaClient = new SaraWSProfilaturaClientImpl(properties);
	}

	@Test
	public void simpleTest() {
		GetRuoliProfiloResponse response = saraWSProfilaturaClient.getRuoliProfilo(codiceFiscale);
		Assert.assertNotNull(response);
		Assert.assertTrue(response.getUtenteTrovato());
		Assert.assertNotNull(response.getRuoli());
	}

}
