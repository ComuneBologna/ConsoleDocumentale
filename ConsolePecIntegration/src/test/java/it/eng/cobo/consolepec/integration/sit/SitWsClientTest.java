package it.eng.cobo.consolepec.integration.sit;

import static org.junit.Assert.assertFalse;

import org.junit.BeforeClass;
import org.junit.Test;

import it.eng.cobo.consolepec.integration.sit.bean.SitResponse;
import it.eng.cobo.consolepec.integration.sit.client.SitWsClient;
import it.eng.cobo.consolepec.integration.sit.client.impl.SitWsClientImpl;
import it.eng.cobo.consolepec.integration.sit.exception.SitWsClientException;

public class SitWsClientTest {

	public static final String SIT_URL = "http://sitmappe.comune.bologna.it/toponomastica_3_1/ricerca";

	private static SitWsClient sitWsClient;

	@BeforeClass
	public static void setUp() {
		sitWsClient = new SitWsClientImpl(SIT_URL);
	}

	@Test
	public void validaVia() throws SitWsClientException {
		SitResponse resp = sitWsClient.validaDescrizioneVia("via dei lamponi");
		assertFalse(resp.isError());
	}

	@Test(expected = SitWsClientException.class)
	public void validaViaErrata() throws SitWsClientException {
		SitResponse resp = sitWsClient.validaDescrizioneVia("via dei lampone");
		assertFalse(resp.isError());
	}

	@Test
	public void validaIndirizzoConCivico() throws SitWsClientException {
		SitResponse resp = sitWsClient.validaIndirizzoCompleto("via dei lamponi", "34");
		assertFalse(resp.isError());
	}

	@Test
	public void validaIndirizzoConCivicoEsponente() throws SitWsClientException {
		SitResponse resp = sitWsClient.validaIndirizzoCompleto("via di s.luca", "15", "3A");
		assertFalse(resp.isError());
	}

	@Test
	public void decodificaCodiceVia() throws SitWsClientException {
		SitResponse resp = sitWsClient.decodificaCodiceVia("30800");
		assertFalse(resp.isError());
	}

	@Test(expected = SitWsClientException.class)
	public void decodificaCodiceViaErrato() throws SitWsClientException {
		SitResponse resp = sitWsClient.decodificaCodiceVia("00001");
		assertFalse(resp.isError());
	}

}
