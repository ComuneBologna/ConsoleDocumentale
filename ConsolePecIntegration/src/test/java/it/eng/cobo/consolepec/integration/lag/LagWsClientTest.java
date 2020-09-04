package it.eng.cobo.consolepec.integration.lag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Properties;

import org.junit.BeforeClass;

import it.eng.cobo.consolepec.integration.lag.bean.PersonaFisicaDto;
import it.eng.cobo.consolepec.integration.lag.client.LagWsClient;

public class LagWsClientTest {

	public static final String SIT_URL = "http://sitmappe.comune.bologna.it/toponomastica_3_1/ricerca";

	private static LagWsClient lagWsClient;

	@BeforeClass
	public static void setUp() throws IOException {
		Properties prop = new Properties();
		prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("test.properties"));
		// lagWsClient = new LagWsClientImpl(prop, new SitWsClientImpl(SIT_URL));
	}

	// @Test
	public void loadCF() throws Exception {
		PersonaFisicaDto dto = lagWsClient.dettaglioPersonaFisicaByCodiceFiscale("spzntn83h16d205k");
		assertEquals("ANTONIO", dto.getNome());
		assertEquals("SPIEZIA", dto.getCognome());

		assertEquals("004078", dto.getCodComuneNascita());
		assertEquals("19830616", dto.getDataNascita());
		assertEquals("037006", dto.getCodComuneResidenza());

		assertEquals("VIA DEI LAMPONI", dto.getVia());
		assertEquals("34", dto.getCivico().toString());
		assertEquals("40137", dto.getCap());
		assertNull(dto.getEsponente());
	}

	// @Test
	public void loadCFR() throws Exception {
		PersonaFisicaDto dto = lagWsClient.dettaglioPersonaFisicaByCodiceFiscale("chtstl77e69z140d");
		assertNotNull(dto);
	}

	// @Test
	public void testIndirizzo() throws Exception {
		PersonaFisicaDto dto = lagWsClient.dettaglioPersonaFisicaByCodiceFiscale("RBBFST53R22A944G");
		assertEquals("FAUSTO", dto.getNome());
		assertEquals("VIA DI S.LUCA", dto.getVia());
		assertEquals("15", dto.getCivico().toString());
		assertEquals("3A", dto.getEsponente());
		assertEquals("40135", dto.getCap());
	}

}
