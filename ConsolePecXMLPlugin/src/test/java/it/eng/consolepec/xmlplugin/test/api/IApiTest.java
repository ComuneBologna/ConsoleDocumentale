package it.eng.consolepec.xmlplugin.test.api;


public interface IApiTest {
	
	public void init();
	
	public void testAgganciaPraticaAFascicolo();

	public void testAggiungiAllegato();

	public void testAggiungiCapofilaFromBA01();

	public void testAggiungiReinoltroAProtocollazione();

	public void testConcludiFascicolo();

	public void testAFirmaAllegati();

	public void testProtocolla();

	public void testProtocollaCapofila();

	public void testRiassegna();

	public void testTermina();
	
	public void testMettiInAffissione();
	
	public void testDownloadAllegato();
	
	public void testRimuoviAllegati();

	public void testAggiungiDatoAggiuntivo();

	public void testRimuoviDatoAggiuntivo();


}
