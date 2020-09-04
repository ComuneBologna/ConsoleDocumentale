package it.eng.consolepec.xmlplugin.test;

import it.eng.consolepec.xmlplugin.factory.DatiPratica.PraticaCollegata;
import it.eng.consolepec.xmlplugin.factory.XMLPraticaFactory;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailIn;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailOut;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.pratica.modulistica.PraticaModulistica;
import it.eng.consolepec.xmlplugin.pratica.personale.FascicoloPersonale;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

public class UpdaterV11Test {

	XMLPraticaFactory xpf = new XMLPraticaFactory();

	@Test
	public void updateFascicoli() throws Exception {
		String xml = retrieveXMLFascicolo();
		Fascicolo fascicolo = (Fascicolo) xpf.loadPratica(new StringReader(xml));

		List<PraticaCollegata> praticheCollegate = fascicolo.getAllPraticheCollegate();
		Assert.assertEquals(4, praticheCollegate.size());

		StringWriter sw = new StringWriter();
		xpf.serializePraticaInstance(sw, fascicolo);
		System.out.println(sw.toString());

	}

	@Test
	public void updateFascicoliPersonali() throws Exception {
		String xml = retrieveXMLFascicoloPersonale();
		Fascicolo fascicolo = xpf.loadPratica(FascicoloPersonale.class, new StringReader(xml));

		StringWriter sw = new StringWriter();
		xpf.serializePraticaInstance(sw, fascicolo);
		System.out.println(sw.toString());

	}
	
	@Test
	public void updateEmailIn() throws Exception {
		String xml = retrieveXMLEmailIn();
		PraticaEmailIn emailIn = xpf.loadPratica(PraticaEmailIn.class, new StringReader(xml));

		List<PraticaCollegata> praticheCollegate = emailIn.getAllPraticheCollegate();
		Assert.assertEquals(1, praticheCollegate.size());

		StringWriter sw = new StringWriter();
		xpf.serializePraticaInstance(sw, emailIn);
		System.out.println(sw.toString());

	}

	@Test
	public void updateEmailOut() throws Exception {
		String xml = retrieveXMLEmailOut();
		PraticaEmailOut emailOut = xpf.loadPratica(PraticaEmailOut.class, new StringReader(xml));

		List<PraticaCollegata> praticheCollegate = emailOut.getAllPraticheCollegate();
		Assert.assertEquals(1, praticheCollegate.size());

		StringWriter sw = new StringWriter();
		xpf.serializePraticaInstance(sw, emailOut);
		System.out.println(sw.toString());

	}

	@Test
	public void updateModulo() throws Exception {
		String xml = retrieveXMLModulo();
		PraticaModulistica praticaModulistica = xpf.loadPratica(PraticaModulistica.class, new StringReader(xml));

		List<PraticaCollegata> praticheCollegate = praticaModulistica.getAllPraticheCollegate();
		Assert.assertEquals(1, praticheCollegate.size());

		StringWriter sw = new StringWriter();
		xpf.serializePraticaInstance(sw, praticaModulistica);
		System.out.println(sw.toString());

	}

	private String retrieveXML(String fileName) throws IOException {
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
		return IOUtils.toString(inputStream);
	}

	private String retrieveXMLFascicoloPersonale() throws IOException {
		return retrieveXML("test_updater_fascicolo_personale.xml");
	}
	
	private String retrieveXMLFascicolo() throws IOException {
		return retrieveXML("test_updater_fascicolo.xml");
	}

	private String retrieveXMLEmailIn() throws IOException {
		return retrieveXML("test_updater_emailin.xml");
	}

	private String retrieveXMLEmailOut() throws IOException {
		return retrieveXML("test_updater_emailout.xml");
	}

	private String retrieveXMLModulo() throws IOException {
		return retrieveXML("test_updater_modulo.xml");
	}

}
