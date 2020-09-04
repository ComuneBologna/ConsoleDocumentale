package it.eng.consolepec.xmlplugin.test;

import it.eng.consolepec.xmlplugin.factory.JsonPraticaFactory;
import it.eng.consolepec.xmlplugin.factory.XMLPraticaFactory;
import it.eng.consolepec.xmlplugin.pratica.comunicazione.Comunicazione;
import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.GestioneComunicazioneTask;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComunicazioneTest {
	Logger logger = LoggerFactory.getLogger(ComunicazioneTest.class);

	@Test
	public void test() {
		logger.info("TEST COMUNICAZIONE");

		Comunicazione comunicazione = TestUtils.compilaComunicazioneDummy();

		XMLPraticaFactory xpf = new XMLPraticaFactory();
		StringWriter sw1 = new StringWriter();
		xpf.serializePraticaInstance(sw1, comunicazione);

		Comunicazione comunicazione2 = xpf.loadPratica(Comunicazione.class, new StringReader(sw1.toString()));
		StringWriter sw2 = new StringWriter();
		xpf.serializePraticaInstance(sw2, comunicazione2);

		Assert.assertEquals(sw1.toString(), sw2.toString());
		Assert.assertEquals(comunicazione, comunicazione2);
		Assert.assertEquals(comunicazione.getDati(), comunicazione2.getDati());

		
		JsonPraticaFactory jpf = new JsonPraticaFactory();
		StringWriter sw3 = new StringWriter();
		jpf.serializePraticaInstance(sw3, comunicazione);
		Comunicazione comunicazione3 = jpf.loadPratica(Comunicazione.class, new StringReader(sw3.toString()));
		
		Assert.assertEquals(comunicazione, comunicazione3);
		Assert.assertEquals(comunicazione.getDati(), comunicazione3.getDati());

		logger.debug("fine");
	}
	
	@Test
	public void testTask() {
		logger.info("TEST TASK COMUNICAZIONE");

		Comunicazione comunicazione = TestUtils.compilaComunicazioneDummy();

		GestioneComunicazioneTask task = XmlPluginUtil.getGestioneTaskCorrente(comunicazione, GestioneComunicazioneTask.class, "Utente");
		Assert.assertNotNull(task);
		
		logger.debug("fine");
	}
}
