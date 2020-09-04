package it.eng.consolepec.xmlplugin.test;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Utente;
import it.eng.consolepec.xmlplugin.factory.MetadatiPratica;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.factory.PraticaFactory;
import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.factory.TaskFactory;
import it.eng.consolepec.xmlplugin.factory.XMLPraticaFactory;
import it.eng.consolepec.xmlplugin.factory.XMLTaskFactory;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailIn;
import it.eng.consolepec.xmlplugin.pratica.factory.FascicoliFactory;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.pratica.modulistica.PraticaModulistica;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.TaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.DatiGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.GestionePECInTask;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test di un possibile utilizzo da parte del servizio di scarico PEC
 * 
 * @author pluttero
 * 
 */
public class DeSerializationTest {
	Logger logger = LoggerFactory.getLogger(DeSerializationTest.class);

	@Test
	public void serialize_loadPraticaFascicolo() {
		PraticaFactory pf = new XMLPraticaFactory();

		// creazione pratica con passaggio ID mail e folderpath
		Fascicolo fascicolo = TestUtils.compilaFascicoloDummy();

		// validazione dati obbligatori
		Assert.assertTrue(fascicolo.getDati().getTitolo() != null);

		// inizializzazione del task
		TaskFascicolo<?> task = null;
		Set<Task<?>> tasks = fascicolo.getTasks();
		for (Task<?> t : tasks)
			if (t.isAttivo())
				task = (TaskFascicolo<?>) t;

		// DatiGestioneFascicoloTask datiTask = TestUtils.compilaDatiTaskGestioneFascicolo();
		// GestioneFascicoloTask task = tf.newTaskInstance(GestioneFascicoloTask.class, fascicolo, datiTask);
		// logger.debug("log dati task {}", task.getDati());

		// get metadati
		Map<MetadatiPratica, Object> metadatiFascicolo = fascicolo.getMetadata();
		for (MetadatiPratica key : metadatiFascicolo.keySet())
			logger.debug("key: {} value: {}", key.getNome(), metadatiFascicolo.get(key));

		// serializza
		StringWriter sw = new StringWriter();

		pf.serializePraticaInstance(sw, fascicolo);

		logger.debug(sw.toString());

		Assert.assertTrue(sw.toString().length() > 0);

		/* CARICAMENTO */

		Fascicolo fascicolo2 = pf.loadPratica(FascicoliFactory.getPraticaClass(TipologiaPratica.FASCICOLO), new StringReader(sw.toString()));

		Assert.assertTrue(fascicolo2.getTasks().size() == 1);

		logger.debug("DATI FASCICOLO DI DETTAGLIO: {}", fascicolo2.getDati().toString());

		Map<MetadatiPratica, Object> metadatiFascicolo2 = fascicolo2.getMetadata();

		Assert.assertTrue(metadatiFascicolo2.equals(metadatiFascicolo));

		Assert.assertTrue(fascicolo2.equals(fascicolo));

		Assert.assertTrue(fascicolo2.getDati().equals(fascicolo.getDati()));

		// equality task
		Assert.assertTrue(task.equals(fascicolo2.getTasks().iterator().next()));

	}

	@Test
	public void serialize_loadPraticaEmail() {
		PraticaFactory pf = new XMLPraticaFactory();
		TaskFactory tf = new XMLTaskFactory();

		// creazione pratica con passaggio ID mail e folderpath
		PraticaEmailIn pec = TestUtils.compilaPraticaDummyCompleta();

		// validazione dati obbligatori
		// Assert.assertTrue(pec.getDati().getDirezione() != null);
		Assert.assertTrue(pec.getDati().getTipo() != null);

		// inizializzazione del task
		DatiGestionePECTask datiTask = TestUtils.compilaDatiTaskGestionePEC();
		GestionePECInTask task = tf.newTaskInstance(GestionePECInTask.class, pec, datiTask);
		logger.debug("log dati task {}", task.getDati());
		Utente inCaricoA = new Utente("test", "nomeTest", "cognomeTest", "matricolaTest", "sntrrt82c21l188t", new Date());
		task.prendiInCarico(inCaricoA);

		// get metadati
		Map<MetadatiPratica, Object> metadatiPec = pec.getMetadata();
		for (MetadatiPratica key : metadatiPec.keySet())
			logger.debug("key: {} value: {}", key.getNome(), metadatiPec.get(key));

		metadatiPec = pec.getMetadata();

		// serializza
		StringWriter sw = new StringWriter();

		pf.serializePraticaInstance(sw, pec);

		logger.debug(sw.toString());

		Assert.assertTrue(sw.toString().length() > 0);

		/* CARICAMENTO */

		PraticaEmailIn pec2 = pf.loadPratica(PraticaEmailIn.class, new StringReader(sw.toString()));

		Assert.assertTrue(pec2.getTasks().size() == 1);

		logger.debug("DATI PEC DI DETTAGLIO: {}", pec2.getDati().toString());

		Map<MetadatiPratica, Object> metadatiPec2 = pec2.getMetadata();

		for (MetadatiPratica key : metadatiPec2.keySet()) {
			logger.debug("key2: {} value2: {}", key.getNome(), metadatiPec2.get(key));
		}

		Assert.assertTrue(metadatiPec2.equals(metadatiPec));

		// stampa metadati dei tasks
		for (Task<?> task2 : pec2.getTasks()) {
			Map<String, Object> task2Meta = task2.getMetadata();
			for (String key : task2.getMetadata().keySet()) {
				logger.debug("key2: {} value2: {}", key, task2Meta.get(key));
			}
		}

		Assert.assertTrue(pec2.equals(pec));

	}

	@Test
	public void serializeXSDError() {
		logger.debug("Test di serializzazione in XML di un contenuto non valido");
		PraticaFactory pf = new XMLPraticaFactory();
		DatiEmail.Builder builder = TestUtils.compilaDummyBuilderDatiEmail();

		builder.setDestinatarioPrincipale(null); // -->condizione di errore
		DatiEmail dati = builder.construct();

		Pratica<DatiEmail> pratica = pf.newPraticaInstance(PraticaEmailIn.class, dati);
		StringWriter sw = new StringWriter();

		pf.serializePraticaInstance(sw, pratica);

		PraticaEmailIn pe = pf.loadPratica(PraticaEmailIn.class, new StringReader(sw.toString()));

		Assert.assertFalse(pe.isMailCompleta());

	}

	@Test
	public void testCambioPratica() {

		logger.debug("Test di serializzazione per il cambio path");
		PraticaFactory pf = new XMLPraticaFactory();
		DatiEmail.Builder builder = TestUtils.compilaDummyBuilderDatiEmail();

		DatiEmail dati = builder.construct();

		Pratica<DatiEmail> pratica = pf.newPraticaInstance(PraticaEmailIn.class, dati);
		StringWriter sw = new StringWriter();

		pf.serializePraticaInstance(sw, pratica);

		logger.info(sw.toString());
		logger.debug("=======================================CAMBIO PATH===================================");

		StringWriter sw2 = new StringWriter();
		PraticaEmailIn pe = pf.loadPratica(PraticaEmailIn.class, new StringReader(sw.toString()));
		pe.cambiaPath("/TEST/PATH/NEW");

		pf.serializePraticaInstance(sw2, pe);

		logger.info(sw2.toString());

		Assert.assertFalse(pe.getDati().getFolderPath().equalsIgnoreCase(pratica.getDati().getFolderPath()));
	}

	@Test
	public void testLoadGenerico() {
		logger.debug("=======================================testLoadGenerico===================================");
		PraticaFactory pf = new XMLPraticaFactory();
		StringWriter sw = new StringWriter();
		PraticaEmailIn email = TestUtils.compilaPraticaDummyCompleta();
		pf.serializePraticaInstance(sw, email);
		Pratica<?> p = pf.loadPratica(new StringReader(sw.toString()));
		Assert.assertTrue(p != null);
		Assert.assertTrue(p.getDati().getTipo().equals(TipologiaPratica.EMAIL_IN));

		Fascicolo fascicolo = TestUtils.compilaFascicoloDummy();
		sw = new StringWriter();
		pf.serializePraticaInstance(sw, fascicolo);

		p = pf.loadPratica(new StringReader(sw.toString()));
		Assert.assertTrue(p != null);
		Assert.assertTrue(p.getDati().getTipo().equals(TipologiaPratica.FASCICOLO_PERSONALE));
		logger.debug("=======================================FINE testLoadGenerico===================================");

	}

	@Test
	public void testLoadSerializeAbilitazioni() {
		PraticaFactory pf = new XMLPraticaFactory();
		StringWriter sw = new StringWriter();
		StringWriter sw1 = new StringWriter();
		PraticaEmailIn email = TestUtils.compilaPraticaDummyCompleta();
		Assert.assertTrue(email.getDati().getGruppiVisibilita().size() == 5);
		pf.serializePraticaInstance(sw, email);
		Pratica<?> p = pf.loadPratica(new StringReader(sw.toString()));
		Assert.assertTrue(p.getDati().getGruppiVisibilita().size() == 5);
		pf.serializePraticaInstance(sw1, p);
		Assert.assertEquals(sw.toString(), sw1.toString());

		Map<String, Object> metadataString = p.getMetadataString();
		for (String s : metadataString.keySet()) {
			logger.info("Metadato: {} = {}.", s, metadataString.get(s));

		}
	}

	@Test
	public void testLoadSerializePraticaModulistica() {

		PraticaModulistica pm = TestUtils.compilaPraticaModulisticaDummy();

		Map<String, Object> metadataString = pm.getMetadataString();
		for (String s : metadataString.keySet()) {
			logger.info("Metadato: {} = {}.", s, metadataString.get(s));

		}
	}
	
	

	
}
