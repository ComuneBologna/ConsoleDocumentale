package it.eng.consolepec.xmlplugin.test;

import it.eng.consolepec.xmlplugin.factory.XMLPraticaFactory;
import it.eng.consolepec.xmlplugin.pratica.modulistica.PraticaModulistica;

import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PraticaModulisticaTest {
	
	Logger logger = LoggerFactory.getLogger(PraticaModulisticaTest.class);

	@Test
	public void testPraticaModulistica() {
		logger.info("TEST PRATICA MODULISTICA");

		PraticaModulistica modulistica = TestUtils.compilaPraticaModulisticaDummy();

		XMLPraticaFactory xpf = new XMLPraticaFactory();
		StringWriter sw1 = new StringWriter();
		xpf.serializePraticaInstance(sw1, modulistica);

		PraticaModulistica modulistica2 = xpf.loadPratica(PraticaModulistica.class, new StringReader(sw1.toString()));
		StringWriter sw2 = new StringWriter();
		xpf.serializePraticaInstance(sw2, modulistica2);

		Assert.assertEquals(sw1.toString(), sw2.toString());
		Assert.assertEquals(modulistica, modulistica2);
		Assert.assertEquals(modulistica.getDati(), modulistica2.getDati());

		
		
		logger.debug("fine");
	}

}
