package it.eng.consolepec.xmlplugin.test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.consolepec.xmlplugin.factory.DatiPratica;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.GruppoVisibilita;
import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.factory.JsonPraticaFactory;
import it.eng.consolepec.xmlplugin.factory.PraticaFactory;
import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.factory.XMLPraticaFactory;
import it.eng.consolepec.xmlplugin.factory.XMLTaskFactory;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.TipoEmail;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailIn;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailOut;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.DatiGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.GestionePECInTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.GestionePECOutTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.GestionePECTask;

public class GestionePECTaskTest {
	private Reader source;
	private Logger logger = LoggerFactory.getLogger(GestionePECTaskTest.class);

	@Before
	public void initPratica() {
		source = TestUtils.getPraticaPECStream();
	}

	private GestionePECTask recuperaTaskGestionePEC(PraticaEmailIn mail) {
		/*
		 * L'operazione di firma può essere solo eseguita da chi ha il task di GEstionePEC, ricerchiamo quindi il task tra quelli della mail. N.B. queste api cambieranno, a seconda di come evolverà la
		 * gestione profilazione
		 */

		Set<Task<?>> tasks = mail.getTasks();
		GestionePECTask task = null;

		for (Task<?> t : tasks) {
			if (t instanceof GestionePECTask && t.isAttivo()) {
				task = (GestionePECInTask) t;
				task.setCurrentUser("UtenteTestPEC");
				task.setOperazioniAbilitate(TestUtils.getOperazioniAbilitate());
				return task;
			}
		}
		return null;
	}

	@Test
	public void riassegnaTest() {
		logger.debug("Test di riassegnazione");
		Assert.assertNotNull(source);
		/* ottengo una pratica da una sorgente Reader */
		PraticaFactory pf = new XMLPraticaFactory();
		final PraticaEmailIn mail = pf.loadPratica(PraticaEmailIn.class, source);

		final GestionePECInTask task = (GestionePECInTask) recuperaTaskGestionePEC(mail);

		task.getDati().getAssegnatariPassati().size();

		AnagraficaRuolo ar = new AnagraficaRuolo();
		ar.setRuolo("Gruppo123");
		ar.setEtichetta("Gruppo123");
		task.riassegna(ar, new ArrayList<GruppoVisibilita>(), "", new ArrayList<String>());
	}

	@Test
	public void firmaAPITest() {
		Assert.assertNotNull(source);
		/* ottengo una pratica da una sorgente Reader */
		PraticaFactory pf = new XMLPraticaFactory();
		InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("metadati_out.xml");
		final PraticaEmailOut mail = pf.loadPratica(PraticaEmailOut.class, new InputStreamReader(resourceAsStream));

		JsonPraticaFactory jsf = new JsonPraticaFactory();
		StringWriter sw = new StringWriter();
		jsf.serializePraticaInstance(sw, mail);

		GestionePECOutTask task = TestUtils.recuperaTaskGestionePECOut(mail);

		// un allegato a caso...
		final Allegato allegato = mail.getDati().getAllegati().first();

		ArrayList<DatiPratica.Allegato> allegati = new ArrayList<DatiPratica.Allegato>();
		allegati.add(allegato);
		task.firmaAllegati(allegati);

	}

	@Test
	public void creaBozzaTest() {

		DatiEmail.Builder builder = new DatiEmail.Builder();

		builder.setIdDocumentale("FS" + 21);
		builder.setMessageIDReinoltro("MessageIDReinoltro");

		DatiEmail datiEmail = builder.construct();

		XMLPraticaFactory xpf = new XMLPraticaFactory();
		PraticaEmailOut emailOut = xpf.newPraticaInstance(PraticaEmailOut.class, datiEmail);

		Assert.assertTrue(emailOut.isReinoltro());

		StringWriter sw = new StringWriter();
		xpf.serializePraticaInstance(sw, emailOut);

		logger.info(sw.toString());

		DatiGestionePECTask.Builder builderTask = new DatiGestionePECTask.Builder();
		builderTask.setAssegnatario(new Assegnatario("UtentiPortale", "", new Date(System.currentTimeMillis() - 1000), new Date(System.currentTimeMillis() + 100000000)));
		builderTask.setAttivo(true);
		DatiGestionePECTask task = builderTask.construct();

		XMLTaskFactory xtf = new XMLTaskFactory();
		GestionePECOutTask pecOutTask = xtf.newTaskInstance(GestionePECOutTask.class, emailOut, task);

		Assert.assertFalse(pecOutTask.isEmailValida());

		StringWriter sw2 = new StringWriter();
		xpf.serializePraticaInstance(sw2, emailOut);

		logger.info(sw2.toString());

		PraticaEmailOut praticaEmailOut = xpf.loadPratica(PraticaEmailOut.class, new StringReader(sw2.toString()));

		GestionePECOutTask taskPecOut = TestUtils.recuperaTaskGestionePECOut(praticaEmailOut);

		taskPecOut.setBody("Prova creazione bozza");
		taskPecOut.setDataCreazione(new Date());
		taskPecOut.setDataInvio(new Date());
		taskPecOut.setDestinatarioPrincipale("roberto.santi@eng.it");
		taskPecOut.setMittente("michele.ricciardi@eng.it");
		taskPecOut.setOggetto("Oggetto email di prova");
		taskPecOut.setStato(Stato.BOZZA);
		taskPecOut.setTipoEmail(TipoEmail.NORMALE);
		taskPecOut.setTitolo("Test Case compilazione email");

		taskPecOut.addAllegato(praticaEmailOut.getDati().new Allegato("Allegato Numero 1", "Allegato Numero 1", praticaEmailOut.getSubFolderPath(), null, null, 345L, "0.1", false, null, false, null,
				null, null, null, null, null));
		taskPecOut.addAllegato(praticaEmailOut.getDati().new Allegato("Allegato Numero 2", "Allegato Numero 2", praticaEmailOut.getSubFolderPath(), null, null, 543L, "0.3", false, null, false, null,
				null, null, null, null, null));

		taskPecOut.addDestinatario(new Destinatario("paolo.lutterotti@eng.it", null, it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario.TipoEmail.esterno, false, false));
		taskPecOut.addDestinatario(new Destinatario("paolo.lutterotti@eng.it2", null, it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario.TipoEmail.esterno, false, false));

		taskPecOut.addDestinatarioCC(new Destinatario("paolo.lutterotti@eng.it3", null, it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario.TipoEmail.esterno, false, false));
		taskPecOut.addDestinatarioCC(new Destinatario("paolo.lutterotti@eng.it4", null, it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario.TipoEmail.esterno, false, false));

		Assert.assertTrue(taskPecOut.isEmailValida());

		StringWriter sw3 = new StringWriter();
		xpf.serializePraticaInstance(sw3, praticaEmailOut);

		logger.info(sw3.toString());

		PraticaEmailOut praticaEmailOut2 = xpf.loadPratica(PraticaEmailOut.class, new StringReader(sw3.toString()));

		StringWriter sw4 = new StringWriter();
		xpf.serializePraticaInstance(sw4, praticaEmailOut2);

		Assert.assertEquals(sw3.toString(), sw4.toString());
	}

	@Test
	public void riportaInLettura() {
		Assert.assertNotNull(source);
		/* ottengo una pratica da una sorgente Reader */
		PraticaFactory pf = new XMLPraticaFactory();
		final PraticaEmailIn mail = pf.loadPratica(PraticaEmailIn.class, source);

		mail.getDati().setLetto(true);

		GestionePECInTask task = (GestionePECInTask) recuperaTaskGestionePEC(mail);

		task.riportaInLettura();

		Assert.assertEquals(mail.getDati().isLetto(), false);
	}

}
