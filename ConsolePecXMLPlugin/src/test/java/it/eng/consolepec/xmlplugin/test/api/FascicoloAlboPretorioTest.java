package it.eng.consolepec.xmlplugin.test.api;

import it.eng.consolepec.xmlplugin.pratica.albopretorio.FascicoloAlboPretorio;
import it.eng.consolepec.xmlplugin.test.TestUtils;

import org.junit.Before;
import org.junit.Test;

public class FascicoloAlboPretorioTest implements IApiTest {
	
	FascicoloAlboPretorio fascicolo;

	@Before
	public void init() {
		fascicolo = TestUtils.compilaFascicoloAlboPretorioDummy();
	}

	@Test
	public void testAgganciaPraticaAFascicolo() {
		// TODO Auto-generated method stub

	}

	@Test
	public void testAggiungiAllegato() {
		// TODO Auto-generated method stub

	}

	@Test
	public void testAggiungiCapofilaFromBA01() {
		// TODO Auto-generated method stub

	}

	@Test
	public void testAggiungiReinoltroAProtocollazione() {
		// TODO Auto-generated method stub

	}

	@Test
	public void testConcludiFascicolo() {
		// TODO Auto-generated method stub

	}

	@Test
	public void testAFirmaAllegati() {
		// TODO Auto-generated method stub

	}

	@Test
	public void testProtocolla() {
		// TODO Auto-generated method stub

	}

	@Test
	public void testProtocollaCapofila() {
		// TODO Auto-generated method stub

	}

	@Test
	public void testRiassegna() {
		// TODO Auto-generated method stub

	}

	@Test
	public void testTermina() {
		// TODO Auto-generated method stub

	}

	@Test
	public void testMettiInAffissione() {
		// TODO Auto-generated method stub

	}

	@Test
	public void testDownloadAllegato() {
		// TODO Auto-generated method stub

	}

	@Override
	public void testRimuoviAllegati() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void testAggiungiDatoAggiuntivo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void testRimuoviDatoAggiuntivo() {
		// TODO Auto-generated method stub
		
	}

}
