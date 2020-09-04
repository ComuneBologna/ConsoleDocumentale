package it.eng.consolepec.xmlplugin.test;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.MetadatiPratica;
import it.eng.consolepec.xmlplugin.factory.XMLPraticaFactory;
import it.eng.consolepec.xmlplugin.pratica.albopretorio.FascicoloAlboPretorio;
import it.eng.consolepec.xmlplugin.pratica.elettorale.FascicoloElettoraleComunicazioni;
import it.eng.consolepec.xmlplugin.pratica.elettorale.FascicoloElettoraleElettore;
import it.eng.consolepec.xmlplugin.pratica.elettorale.FascicoloElettoraleGenerico;
import it.eng.consolepec.xmlplugin.pratica.factory.FascicoliFactory;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.pratica.fatturazione.FascicoloFatturazione;
import it.eng.consolepec.xmlplugin.pratica.modulistica.FascicoloModulistica;
import it.eng.consolepec.xmlplugin.pratica.personale.FascicoloPersonale;
import it.eng.consolepec.xmlplugin.pratica.riservato.FascicoloRiservato;
import it.eng.consolepec.xmlplugin.pratica.sport.FascicoloSportBorgoPanigale;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FascicoliTest {

	Logger logger = LoggerFactory.getLogger(FascicoliTest.class);

	@Test
	public void testFascicolo() {
		logger.info("TEST FASCICOLO GENERICO");

		Fascicolo fascicolo = TestUtils.compilaFascicoloDummy();

		XMLPraticaFactory xpf = new XMLPraticaFactory();
		StringWriter sw1 = new StringWriter();
		xpf.serializePraticaInstance(sw1, fascicolo);

		Fascicolo fascicolo2 = xpf.loadPratica(FascicoliFactory.getPraticaClass(TipologiaPratica.FASCICOLO), new StringReader(sw1.toString()));
		Assert.assertEquals(fascicolo.getDati().getTipo(), TipologiaPratica.FASCICOLO_PERSONALE);
		Assert.assertEquals(fascicolo2.getDati().getTipo(), TipologiaPratica.FASCICOLO_PERSONALE);
		Assert.assertEquals(fascicolo, fascicolo2);
		Assert.assertEquals(fascicolo.getDati(), fascicolo2.getDati());

		StringWriter sw2 = new StringWriter();
		xpf.serializePraticaInstance(sw2, fascicolo2);

		Assert.assertEquals(sw1.toString(), sw2.toString());

		Map<MetadatiPratica, Object> metadata = fascicolo2.getMetadata();
		for (MetadatiPratica p : metadata.keySet())
			logger.debug("metadato -- {} : {}", p.name(), metadata.get(p));
		logger.debug("fine");
	}

	@Test
	public void testFascicoloalboPretorio() {
		logger.info("TEST FASCICOLO ALBO PRETORIO");
		FascicoloAlboPretorio fascicoloAlboPretorio = TestUtils.compilaFascicoloAlboPretorioDummy();

		XMLPraticaFactory xpf = new XMLPraticaFactory();
		StringWriter sw1 = new StringWriter();
		xpf.serializePraticaInstance(sw1, fascicoloAlboPretorio);
		logger.debug("Originale serializzato filippo: {}", sw1.toString());
		FascicoloAlboPretorio fascicoloAlboPretorio2 = xpf.loadPratica(FascicoloAlboPretorio.class, new StringReader(sw1.toString()));

		StringWriter sw0 = new StringWriter();
		xpf.serializePraticaInstance(sw0, fascicoloAlboPretorio2);
		logger.debug("Copia deserializzata riserializzata filippo: {}", sw0.toString());
		
		printFascicoloToFile("/home/roger/Confronti_meld/fascicolo1.txt", sw0);
		printFascicoloToFile("/home/roger/Confronti_meld/fascicolo2.txt", sw1);

		Assert.assertEquals(sw1.toString(), sw0.toString());
		Assert.assertEquals(fascicoloAlboPretorio.getDati().getTipo(), TipologiaPratica.FASCICOLO_ALBO_PRETORIO);
		Assert.assertEquals(fascicoloAlboPretorio2.getDati().getTipo(), TipologiaPratica.FASCICOLO_ALBO_PRETORIO);
		Assert.assertEquals(fascicoloAlboPretorio, fascicoloAlboPretorio2);

		logger.debug("FASCICOLO 1");

		for (Allegato a : fascicoloAlboPretorio.getDati().getAllegati())
			logger.debug(a.toString());

		logger.debug("FASCICOLO 2");

		for (Allegato a : fascicoloAlboPretorio2.getDati().getAllegati())
			logger.debug(a.toString());

		logger.debug(fascicoloAlboPretorio.getDati().toString());
		logger.debug(fascicoloAlboPretorio2.getDati().toString());

		Assert.assertEquals(fascicoloAlboPretorio.getDati(), fascicoloAlboPretorio2.getDati());

		StringWriter sw2 = new StringWriter();
		xpf.serializePraticaInstance(sw2, fascicoloAlboPretorio2);

		Assert.assertEquals(sw1.toString(), sw2.toString());

	}

	@Test
	public void testFascicoloRiservato() {
		logger.info("TEST FASCICOLO RISERVATO");
		FascicoloRiservato fascicoloRiservato = TestUtils.compilaFascicoloRiservatoDummy();
		XMLPraticaFactory xpf = new XMLPraticaFactory();
		StringWriter sw1 = new StringWriter();
		xpf.serializePraticaInstance(sw1, fascicoloRiservato);

		FascicoloRiservato fascicoloRiservato2 = (FascicoloRiservato) xpf.loadPratica(FascicoliFactory.getPraticaClass(TipologiaPratica.FASCICOLO_RISERVATO), new StringReader(sw1.toString()));
		Assert.assertEquals(fascicoloRiservato.getDati().getTipo(), TipologiaPratica.FASCICOLO_RISERVATO);
		Assert.assertEquals(fascicoloRiservato2.getDati().getTipo(), TipologiaPratica.FASCICOLO_RISERVATO);
		Assert.assertEquals(fascicoloRiservato, fascicoloRiservato2);
		Assert.assertEquals(fascicoloRiservato.getDati(), fascicoloRiservato2.getDati());

		StringWriter sw2 = new StringWriter();
		xpf.serializePraticaInstance(sw2, fascicoloRiservato2);

		Assert.assertEquals(sw1.toString(), sw2.toString());

	}
	
	@Test
	public void testFascicoloPersonale() {
		logger.info("TEST FASCICOLO PERSONALE");
		FascicoloPersonale fascicoloPersonale = TestUtils.compilaFascicoloPersonaleDummy();
		XMLPraticaFactory xpf = new XMLPraticaFactory();
		StringWriter sw1 = new StringWriter();
		xpf.serializePraticaInstance(sw1, fascicoloPersonale);

		FascicoloPersonale fascicoloPersonale2 = (FascicoloPersonale) xpf.loadPratica(FascicoliFactory.getPraticaClass(TipologiaPratica.FASCICOLO_PERSONALE), new StringReader(sw1.toString()));
		Assert.assertEquals(fascicoloPersonale.getDati().getTipo(), TipologiaPratica.FASCICOLO_PERSONALE);
		Assert.assertEquals(fascicoloPersonale2.getDati().getTipo(), TipologiaPratica.FASCICOLO_PERSONALE);
		Assert.assertEquals(fascicoloPersonale, fascicoloPersonale2);
		Assert.assertEquals(fascicoloPersonale.getDati(), fascicoloPersonale2.getDati());

		StringWriter sw2 = new StringWriter();
		xpf.serializePraticaInstance(sw2, fascicoloPersonale2);

		Assert.assertEquals(sw1.toString(), sw2.toString());

	}

	@Test
	public void testFascicoloalboFatturazione() {
		logger.info("TEST FASCICOLO FATTURAZIONE");
		FascicoloFatturazione fascicoloFatturazione = TestUtils.compilaFascicoloFatturazioneDummy();

		XMLPraticaFactory xpf = new XMLPraticaFactory();
		StringWriter sw1 = new StringWriter();
		xpf.serializePraticaInstance(sw1, fascicoloFatturazione);
		logger.debug("Originale serializzato filippo: {}", sw1.toString());
		FascicoloFatturazione fascicoloFatturazione2 = xpf.loadPratica(FascicoloFatturazione.class, new StringReader(sw1.toString()));

		StringWriter sw0 = new StringWriter();
		xpf.serializePraticaInstance(sw0, fascicoloFatturazione2);
		logger.debug("Copia deserializzata riserializzata filippo: {}", sw0.toString());

		Assert.assertEquals(sw1.toString(), sw0.toString());
		Assert.assertEquals(fascicoloFatturazione.getDati().getTipo(), TipologiaPratica.FASCICOLO_FATTURAZIONE_ELETTRONICA);
		Assert.assertEquals(fascicoloFatturazione2.getDati().getTipo(), TipologiaPratica.FASCICOLO_FATTURAZIONE_ELETTRONICA);
		Assert.assertEquals(fascicoloFatturazione, fascicoloFatturazione2);

		logger.debug("FASCICOLO 1");

		for (Allegato a : fascicoloFatturazione.getDati().getAllegati())
			logger.debug(a.toString());

		logger.debug("FASCICOLO 2");

		for (Allegato a : fascicoloFatturazione2.getDati().getAllegati())
			logger.debug(a.toString());

		logger.debug(fascicoloFatturazione.getDati().toString());
		logger.debug(fascicoloFatturazione2.getDati().toString());

		Assert.assertEquals(fascicoloFatturazione.getDati(), fascicoloFatturazione2.getDati());

		StringWriter sw2 = new StringWriter();
		xpf.serializePraticaInstance(sw2, fascicoloFatturazione2);

		Assert.assertEquals(sw1.toString(), sw2.toString());

	}
	
	@Test
	public void testFascicolModulistica() {
		logger.info("TEST FASCICOLO MODULISTICA");
		FascicoloModulistica fascicoloModulistica = TestUtils.compilaFascicoloModulisticaDummy();

		XMLPraticaFactory xpf = new XMLPraticaFactory();
		StringWriter sw1 = new StringWriter();
		xpf.serializePraticaInstance(sw1, fascicoloModulistica);
		logger.debug("Originale serializzato filippo: {}", sw1.toString());
		FascicoloModulistica fascicoloModulistica2 = xpf.loadPratica(FascicoloModulistica.class, new StringReader(sw1.toString()));

		StringWriter sw0 = new StringWriter();
		xpf.serializePraticaInstance(sw0, fascicoloModulistica2);
		logger.debug("Copia deserializzata riserializzata filippo: {}", sw0.toString());

		Assert.assertEquals(sw1.toString(), sw0.toString());
		Assert.assertEquals(fascicoloModulistica.getDati().getTipo(), TipologiaPratica.FASCICOLO_MODULISTICA);
		Assert.assertEquals(fascicoloModulistica2.getDati().getTipo(), TipologiaPratica.FASCICOLO_MODULISTICA);
		Assert.assertEquals(fascicoloModulistica, fascicoloModulistica2);

		logger.debug("FASCICOLO 1");

		for (Allegato a : fascicoloModulistica.getDati().getAllegati())
			logger.debug(a.toString());

		logger.debug("FASCICOLO 2");

		for (Allegato a : fascicoloModulistica2.getDati().getAllegati())
			logger.debug(a.toString());

		logger.debug(fascicoloModulistica.getDati().toString());
		logger.debug(fascicoloModulistica2.getDati().toString());

		Assert.assertEquals(fascicoloModulistica.getDati(), fascicoloModulistica2.getDati());

		StringWriter sw2 = new StringWriter();
		xpf.serializePraticaInstance(sw2, fascicoloModulistica2);

		Assert.assertEquals(sw1.toString(), sw2.toString());

	}
	@Test
	public void testFascicolSport() {
		logger.info("TEST FASCICOLO SPORT");
		FascicoloSportBorgoPanigale fascicoloSport = TestUtils.compilaFascicoloSportDummy();

		XMLPraticaFactory xpf = new XMLPraticaFactory();
		StringWriter sw1 = new StringWriter();
		xpf.serializePraticaInstance(sw1, fascicoloSport);
		logger.debug("Originale serializzato filippo: {}", sw1.toString());
		FascicoloSportBorgoPanigale fascicoloSport2 = xpf.loadPratica(FascicoloSportBorgoPanigale.class, new StringReader(sw1.toString()));

		StringWriter sw0 = new StringWriter();
		xpf.serializePraticaInstance(sw0, fascicoloSport2);
		logger.debug("Copia deserializzata riserializzata filippo: {}", sw0.toString());

		Assert.assertEquals(sw1.toString(), sw0.toString());
		Assert.assertEquals(fascicoloSport.getDati().getTipo(), TipologiaPratica.FASCICOLO_SPORT_BORGOPANIGALE);
		Assert.assertEquals(fascicoloSport2.getDati().getTipo(), TipologiaPratica.FASCICOLO_SPORT_BORGOPANIGALE);
		Assert.assertEquals(fascicoloSport, fascicoloSport2);

		logger.debug("FASCICOLO 1");

		for (Allegato a : fascicoloSport.getDati().getAllegati())
			logger.debug(a.toString());

		logger.debug("FASCICOLO 2");

		for (Allegato a : fascicoloSport2.getDati().getAllegati())
			logger.debug(a.toString());

		logger.debug(fascicoloSport.getDati().toString());
		logger.debug(fascicoloSport2.getDati().toString());

		Assert.assertEquals(fascicoloSport.getDati(), fascicoloSport2.getDati());

		StringWriter sw2 = new StringWriter();
		xpf.serializePraticaInstance(sw2, fascicoloSport2);

		Assert.assertEquals(sw1.toString(), sw2.toString());

	}
	@Test
	public void testFascicoloGareSubappalto() {
		logger.info("TEST FASCICOLO GARE SUBAPPLATO");
		FascicoloAlboPretorio fascicoloAlboPretorio = TestUtils.compilaFascicoloAlboPretorioDummy();

		XMLPraticaFactory xpf = new XMLPraticaFactory();
		StringWriter sw1 = new StringWriter();
		xpf.serializePraticaInstance(sw1, fascicoloAlboPretorio);
		logger.debug("Originale serializzato filippo: {}", sw1.toString());
		FascicoloAlboPretorio fascicoloAlboPretorio2 = xpf.loadPratica(FascicoloAlboPretorio.class, new StringReader(sw1.toString()));

		StringWriter sw0 = new StringWriter();
		xpf.serializePraticaInstance(sw0, fascicoloAlboPretorio2);
		logger.debug("Copia deserializzata riserializzata filippo: {}", sw0.toString());
		
		printFascicoloToFile("/home/roger/Confronti_meld/fascicolo1.txt", sw0);
		printFascicoloToFile("/home/roger/Confronti_meld/fascicolo2.txt", sw1);

		Assert.assertEquals(sw1.toString(), sw0.toString());
		Assert.assertEquals(fascicoloAlboPretorio.getDati().getTipo(), TipologiaPratica.FASCICOLO_ALBO_PRETORIO);
		Assert.assertEquals(fascicoloAlboPretorio2.getDati().getTipo(), TipologiaPratica.FASCICOLO_ALBO_PRETORIO);
		Assert.assertEquals(fascicoloAlboPretorio, fascicoloAlboPretorio2);

		logger.debug("FASCICOLO 1");

		for (Allegato a : fascicoloAlboPretorio.getDati().getAllegati())
			logger.debug(a.toString());

		logger.debug("FASCICOLO 2");

		for (Allegato a : fascicoloAlboPretorio2.getDati().getAllegati())
			logger.debug(a.toString());

		logger.debug(fascicoloAlboPretorio.getDati().toString());
		logger.debug(fascicoloAlboPretorio2.getDati().toString());

		Assert.assertEquals(fascicoloAlboPretorio.getDati(), fascicoloAlboPretorio2.getDati());

		StringWriter sw2 = new StringWriter();
		xpf.serializePraticaInstance(sw2, fascicoloAlboPretorio2);

		Assert.assertEquals(sw1.toString(), sw2.toString());

	}
	@Test
	public void testFascicoloGariContratti() {
		logger.info("TEST FASCICOLO ALBO PRETORIO");
		FascicoloAlboPretorio fascicoloAlboPretorio = TestUtils.compilaFascicoloAlboPretorioDummy();

		XMLPraticaFactory xpf = new XMLPraticaFactory();
		StringWriter sw1 = new StringWriter();
		xpf.serializePraticaInstance(sw1, fascicoloAlboPretorio);
		logger.debug("Originale serializzato filippo: {}", sw1.toString());
		FascicoloAlboPretorio fascicoloAlboPretorio2 = xpf.loadPratica(FascicoloAlboPretorio.class, new StringReader(sw1.toString()));

		StringWriter sw0 = new StringWriter();
		xpf.serializePraticaInstance(sw0, fascicoloAlboPretorio2);
		logger.debug("Copia deserializzata riserializzata filippo: {}", sw0.toString());
		
		printFascicoloToFile("/home/roger/Confronti_meld/fascicolo1.txt", sw0);
		printFascicoloToFile("/home/roger/Confronti_meld/fascicolo2.txt", sw1);

		Assert.assertEquals(sw1.toString(), sw0.toString());
		Assert.assertEquals(fascicoloAlboPretorio.getDati().getTipo(), TipologiaPratica.FASCICOLO_ALBO_PRETORIO);
		Assert.assertEquals(fascicoloAlboPretorio2.getDati().getTipo(), TipologiaPratica.FASCICOLO_ALBO_PRETORIO);
		Assert.assertEquals(fascicoloAlboPretorio, fascicoloAlboPretorio2);

		logger.debug("FASCICOLO 1");

		for (Allegato a : fascicoloAlboPretorio.getDati().getAllegati())
			logger.debug(a.toString());

		logger.debug("FASCICOLO 2");

		for (Allegato a : fascicoloAlboPretorio2.getDati().getAllegati())
			logger.debug(a.toString());

		logger.debug(fascicoloAlboPretorio.getDati().toString());
		logger.debug(fascicoloAlboPretorio2.getDati().toString());

		Assert.assertEquals(fascicoloAlboPretorio.getDati(), fascicoloAlboPretorio2.getDati());

		StringWriter sw2 = new StringWriter();
		xpf.serializePraticaInstance(sw2, fascicoloAlboPretorio2);

		Assert.assertEquals(sw1.toString(), sw2.toString());

	}
	
	@Test
	public void testFascicoloGareAtti() {
		logger.info("TEST FASCICOLO ALBO PRETORIO");
		FascicoloAlboPretorio fascicoloAlboPretorio = TestUtils.compilaFascicoloAlboPretorioDummy();

		XMLPraticaFactory xpf = new XMLPraticaFactory();
		StringWriter sw1 = new StringWriter();
		xpf.serializePraticaInstance(sw1, fascicoloAlboPretorio);
		logger.debug("Originale serializzato filippo: {}", sw1.toString());
		FascicoloAlboPretorio fascicoloAlboPretorio2 = xpf.loadPratica(FascicoloAlboPretorio.class, new StringReader(sw1.toString()));

		StringWriter sw0 = new StringWriter();
		xpf.serializePraticaInstance(sw0, fascicoloAlboPretorio2);
		logger.debug("Copia deserializzata riserializzata filippo: {}", sw0.toString());
		
		printFascicoloToFile("/home/roger/Confronti_meld/fascicolo1.txt", sw0);
		printFascicoloToFile("/home/roger/Confronti_meld/fascicolo2.txt", sw1);

		Assert.assertEquals(sw1.toString(), sw0.toString());
		Assert.assertEquals(fascicoloAlboPretorio.getDati().getTipo(), TipologiaPratica.FASCICOLO_ALBO_PRETORIO);
		Assert.assertEquals(fascicoloAlboPretorio2.getDati().getTipo(), TipologiaPratica.FASCICOLO_ALBO_PRETORIO);
		Assert.assertEquals(fascicoloAlboPretorio, fascicoloAlboPretorio2);

		logger.debug("FASCICOLO 1");

		for (Allegato a : fascicoloAlboPretorio.getDati().getAllegati())
			logger.debug(a.toString());

		logger.debug("FASCICOLO 2");

		for (Allegato a : fascicoloAlboPretorio2.getDati().getAllegati())
			logger.debug(a.toString());

		logger.debug(fascicoloAlboPretorio.getDati().toString());
		logger.debug(fascicoloAlboPretorio2.getDati().toString());

		Assert.assertEquals(fascicoloAlboPretorio.getDati(), fascicoloAlboPretorio2.getDati());

		StringWriter sw2 = new StringWriter();
		xpf.serializePraticaInstance(sw2, fascicoloAlboPretorio2);

		Assert.assertEquals(sw1.toString(), sw2.toString());
	}
	
	@Test
	public void testFascicoloElettoraleElettore() {
		logger.info("TEST FASCICOLO ELETTORALE ELETTORE");
		FascicoloElettoraleElettore fascicolo = TestUtils.compilaFascicoloElettoraleElettoreDummy();
		XMLPraticaFactory xpf = new XMLPraticaFactory();
		StringWriter sw1 = new StringWriter();
		xpf.serializePraticaInstance(sw1, fascicolo);
		logger.debug("Originale serializzato filippo: {}", sw1.toString());
		FascicoloElettoraleElettore fascicolo2 = xpf.loadPratica(FascicoloElettoraleElettore.class, new StringReader(sw1.toString()));
		StringWriter sw0 = new StringWriter();
		xpf.serializePraticaInstance(sw0, fascicolo2);
		logger.debug("Copia deserializzata riserializzata filippo: {}", sw0.toString());
		//printFascicoloToFile("/home/roger/Confronti_meld/fascicolo1.txt", sw0);
		//printFascicoloToFile("/home/roger/Confronti_meld/fascicolo2.txt", sw1);
		Assert.assertEquals(sw1.toString(), sw0.toString());
		Assert.assertEquals(fascicolo2.getDati().getTipo(), TipologiaPratica.FASCICOLO_ELETTORALE_ELETTORE);
		Assert.assertEquals(fascicolo2.getDati().getTipo(), TipologiaPratica.FASCICOLO_ELETTORALE_ELETTORE);
		Assert.assertEquals(fascicolo, fascicolo2);
		logger.debug("FASCICOLO 1");
		for (Allegato a : fascicolo.getDati().getAllegati())
			logger.debug(a.toString());
		logger.debug("FASCICOLO 2");
		for (Allegato a : fascicolo2.getDati().getAllegati())
			logger.debug(a.toString());
		logger.debug(fascicolo.getDati().toString());
		logger.debug(fascicolo2.getDati().toString());
		Assert.assertEquals(fascicolo.getDati(), fascicolo2.getDati());
		StringWriter sw2 = new StringWriter();
		xpf.serializePraticaInstance(sw2, fascicolo2);
		Assert.assertEquals(sw1.toString(), sw2.toString());
	}
	
	@Test
	public void testFascicoloElettoraleComunicazioni() {
		logger.info("TEST FASCICOLO ELETTORALE COMUNICAZIONI");
		FascicoloElettoraleComunicazioni fascicolo = TestUtils.compilaFascicoloElettoraleComunicazioniDummy();
		XMLPraticaFactory xpf = new XMLPraticaFactory();
		StringWriter sw1 = new StringWriter();
		xpf.serializePraticaInstance(sw1, fascicolo);
		logger.debug("Originale serializzato filippo: {}", sw1.toString());
		FascicoloElettoraleComunicazioni fascicolo2 = xpf.loadPratica(FascicoloElettoraleComunicazioni.class, new StringReader(sw1.toString()));
		StringWriter sw0 = new StringWriter();
		xpf.serializePraticaInstance(sw0, fascicolo2);
		logger.debug("Copia deserializzata riserializzata filippo: {}", sw0.toString());
		//printFascicoloToFile("/home/roger/Confronti_meld/fascicolo1.txt", sw0);
		//printFascicoloToFile("/home/roger/Confronti_meld/fascicolo2.txt", sw1);
		Assert.assertEquals(sw1.toString(), sw0.toString());
		Assert.assertEquals(fascicolo2.getDati().getTipo(), TipologiaPratica.FASCICOLO_ELETTORALE_COMUNICAZIONI);
		Assert.assertEquals(fascicolo2.getDati().getTipo(), TipologiaPratica.FASCICOLO_ELETTORALE_COMUNICAZIONI);
		Assert.assertEquals(fascicolo, fascicolo2);
		logger.debug("FASCICOLO 1");
		for (Allegato a : fascicolo.getDati().getAllegati())
			logger.debug(a.toString());
		logger.debug("FASCICOLO 2");
		for (Allegato a : fascicolo2.getDati().getAllegati())
			logger.debug(a.toString());
		logger.debug(fascicolo.getDati().toString());
		logger.debug(fascicolo2.getDati().toString());
		Assert.assertEquals(fascicolo.getDati(), fascicolo2.getDati());
		StringWriter sw2 = new StringWriter();
		xpf.serializePraticaInstance(sw2, fascicolo2);
		Assert.assertEquals(sw1.toString(), sw2.toString());
	}
	
	@Test
	public void testFascicoloElettoraleGenerico() {
		logger.info("TEST FASCICOLO ELETTORALE GENERICO");
		FascicoloElettoraleGenerico fascicolo = TestUtils.compilaFascicoloElettoraleGenericoDummy();
		XMLPraticaFactory xpf = new XMLPraticaFactory();
		StringWriter sw1 = new StringWriter();
		xpf.serializePraticaInstance(sw1, fascicolo);
		logger.debug("Originale serializzato filippo: {}", sw1.toString());
		FascicoloElettoraleGenerico fascicolo2 = xpf.loadPratica(FascicoloElettoraleGenerico.class, new StringReader(sw1.toString()));
		StringWriter sw0 = new StringWriter();
		xpf.serializePraticaInstance(sw0, fascicolo2);
		logger.debug("Copia deserializzata riserializzata filippo: {}", sw0.toString());
		//printFascicoloToFile("/home/roger/Confronti_meld/fascicolo1.txt", sw0);
		//printFascicoloToFile("/home/roger/Confronti_meld/fascicolo2.txt", sw1);
		Assert.assertEquals(sw1.toString(), sw0.toString());
		Assert.assertEquals(fascicolo2.getDati().getTipo(), TipologiaPratica.FASCICOLO_ELETTORALE_GENERICO);
		Assert.assertEquals(fascicolo2.getDati().getTipo(), TipologiaPratica.FASCICOLO_ELETTORALE_GENERICO);
		Assert.assertEquals(fascicolo, fascicolo2);
		logger.debug("FASCICOLO 1");
		for (Allegato a : fascicolo.getDati().getAllegati())
			logger.debug(a.toString());
		logger.debug("FASCICOLO 2");
		for (Allegato a : fascicolo2.getDati().getAllegati())
			logger.debug(a.toString());
		logger.debug(fascicolo.getDati().toString());
		logger.debug(fascicolo2.getDati().toString());
		Assert.assertEquals(fascicolo.getDati(), fascicolo2.getDati());
		StringWriter sw2 = new StringWriter();
		xpf.serializePraticaInstance(sw2, fascicolo2);
		Assert.assertEquals(sw1.toString(), sw2.toString());
	}
	
	@Test
	public void testFascicoloG() {
		logger.info("TEST FASCICOLO ALBO PRETORIO");
		FascicoloAlboPretorio fascicoloAlboPretorio = TestUtils.compilaFascicoloAlboPretorioDummy();

		XMLPraticaFactory xpf = new XMLPraticaFactory();
		StringWriter sw1 = new StringWriter();
		xpf.serializePraticaInstance(sw1, fascicoloAlboPretorio);
		logger.debug("Originale serializzato filippo: {}", sw1.toString());
		FascicoloAlboPretorio fascicoloAlboPretorio2 = xpf.loadPratica(FascicoloAlboPretorio.class, new StringReader(sw1.toString()));

		StringWriter sw0 = new StringWriter();
		xpf.serializePraticaInstance(sw0, fascicoloAlboPretorio2);
		logger.debug("Copia deserializzata riserializzata filippo: {}", sw0.toString());
		
		printFascicoloToFile("/home/roger/Confronti_meld/fascicolo1.txt", sw0);
		printFascicoloToFile("/home/roger/Confronti_meld/fascicolo2.txt", sw1);

		Assert.assertEquals(sw1.toString(), sw0.toString());
		Assert.assertEquals(fascicoloAlboPretorio.getDati().getTipo(), TipologiaPratica.FASCICOLO_ALBO_PRETORIO);
		Assert.assertEquals(fascicoloAlboPretorio2.getDati().getTipo(), TipologiaPratica.FASCICOLO_ALBO_PRETORIO);
		Assert.assertEquals(fascicoloAlboPretorio, fascicoloAlboPretorio2);

		logger.debug("FASCICOLO 1");

		for (Allegato a : fascicoloAlboPretorio.getDati().getAllegati())
			logger.debug(a.toString());

		logger.debug("FASCICOLO 2");

		for (Allegato a : fascicoloAlboPretorio2.getDati().getAllegati())
			logger.debug(a.toString());

		logger.debug(fascicoloAlboPretorio.getDati().toString());
		logger.debug(fascicoloAlboPretorio2.getDati().toString());

		Assert.assertEquals(fascicoloAlboPretorio.getDati(), fascicoloAlboPretorio2.getDati());

		StringWriter sw2 = new StringWriter();
		xpf.serializePraticaInstance(sw2, fascicoloAlboPretorio2);

		Assert.assertEquals(sw1.toString(), sw2.toString());
	}

	@Test
	public void testFascicoloGenerico() {
		logger.info("TEST FASCICOLO SCARTI RECUPERO");
		Fascicolo fascicolo = TestUtils.compilaFascicoloGenericoDummy();
		XMLPraticaFactory xpf = new XMLPraticaFactory();
		StringWriter sw1 = new StringWriter();
		xpf.serializePraticaInstance(sw1, fascicolo);
		logger.debug("Originale serializzato filippo: {}", sw1.toString());
		Fascicolo fascicolo2 = xpf.loadPratica(Fascicolo.class, new StringReader(sw1.toString()));
		StringWriter sw0 = new StringWriter();
		xpf.serializePraticaInstance(sw0, fascicolo2);
		logger.debug("Copia deserializzata riserializzata filippo: {}", sw0.toString());
		//printFascicoloToFile("/home/roger/Confronti_meld/fascicolo1.txt", sw0);
		//printFascicoloToFile("/home/roger/Confronti_meld/fascicolo2.txt", sw1);
		Assert.assertEquals(sw1.toString(), sw0.toString());
		Assert.assertEquals(fascicolo2.getDati().getTipo().getNomeTipologia(), "FASCICOLO_GENERICO");
		Assert.assertEquals(fascicolo2.getDati().getTipo().getNomeTipologia(), "FASCICOLO_GENERICO");
		Assert.assertEquals(fascicolo, fascicolo2);
		logger.debug("FASCICOLO 1");
		for (Allegato a : fascicolo.getDati().getAllegati())
			logger.debug(a.toString());
		logger.debug("FASCICOLO 2");
		for (Allegato a : fascicolo2.getDati().getAllegati())
			logger.debug(a.toString());
		logger.debug(fascicolo.getDati().toString());
		logger.debug(fascicolo2.getDati().toString());
		Assert.assertEquals(fascicolo.getDati(), fascicolo2.getDati());
		StringWriter sw2 = new StringWriter();
		xpf.serializePraticaInstance(sw2, fascicolo2);
		Assert.assertEquals(sw1.toString(), sw2.toString());
	}

	
	private static void printFascicoloToFile(String pathFile, StringWriter sw) {
		Writer writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathFile), "utf-8"));
			writer.write(sw.toString());
		} catch (IOException ex) {
			// report
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {
			}
		}
	}

}
