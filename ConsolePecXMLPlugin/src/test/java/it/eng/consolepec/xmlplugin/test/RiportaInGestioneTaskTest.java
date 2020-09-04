package it.eng.consolepec.xmlplugin.test;

import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.factory.XMLPraticaFactory;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailIn;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.GestionePECInTask;
import it.eng.consolepec.xmlplugin.tasks.riattiva.RiattivaPECInTask;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RiportaInGestioneTaskTest {

	private Reader source;
	private Logger logger = LoggerFactory.getLogger(RiportaInGestioneTaskTest.class);

	@Before
	public void initPratica() {
		source = TestUtils.getPraticaPECStream();
	}

	@Test
	public void riportaInLavorazioneTest() {
		XMLPraticaFactory xpf = new XMLPraticaFactory();
		PraticaEmailIn praticaEmailIn = xpf.loadPratica(PraticaEmailIn.class, source);

		Set<Task<?>> tasks = praticaEmailIn.getTasks();
		GestionePECInTask gestionePECInTask = null;
		for (Task<?> task : tasks) {
			if (task instanceof GestionePECInTask && task.isAttivo()) {
				gestionePECInTask = (GestionePECInTask) task;
				gestionePECInTask.setCurrentUser("UtenteTestPEC");
				break;
			}
		}
		gestionePECInTask.setOperazioniAbilitate(TestUtils.getOperazioniAbilitate());
		
		gestionePECInTask.archivia();

		StringWriter writer = new StringWriter();
		xpf.serializePraticaInstance(writer, praticaEmailIn);

		PraticaEmailIn praticaEmailIn2 = xpf.loadPratica(PraticaEmailIn.class, new StringReader(writer.toString()));

		StringWriter writer2 = new StringWriter();
		xpf.serializePraticaInstance(writer2, praticaEmailIn2);

		logger.info(writer2.toString());
		logger.info(writer.toString());
		Assert.assertEquals(writer2.toString(), writer.toString());
		Assert.assertEquals(praticaEmailIn.getDati(), praticaEmailIn2.getDati());
		Assert.assertEquals(praticaEmailIn, praticaEmailIn2);
		Assert.assertTrue(praticaEmailIn.getDati().getStato().equals(praticaEmailIn2.getDati().getStato()));

		Assert.assertTrue(praticaEmailIn.getDati().getStato().equals(Stato.ARCHIVIATA));

		gestionePECInTask = null;
		for (Task<?> task : tasks) {
			if (task instanceof GestionePECInTask && task.isAttivo()) {
				gestionePECInTask = (GestionePECInTask) task;
				break;
			}
		}
		Assert.assertTrue(gestionePECInTask == null);

		RiattivaPECInTask riportaInGestionePECInTask = null;
		for (Task<?> task : praticaEmailIn.getTasks()) {
			if (task instanceof RiattivaPECInTask && task.isAttivo()) {
				riportaInGestionePECInTask = (RiattivaPECInTask) task;
				break;
			}
		}
		if (praticaEmailIn.isRiattivabile())
			riportaInGestionePECInTask.riattivaTaskAssociato();

		for (Task<?> task : praticaEmailIn2.getTasks()) {
			if (task instanceof RiattivaPECInTask && task.isAttivo()) {
				riportaInGestionePECInTask = (RiattivaPECInTask) task;
				break;
			}
		}
		if (praticaEmailIn2.isRiattivabile())
			riportaInGestionePECInTask.riattivaTaskAssociato();
	}

}
