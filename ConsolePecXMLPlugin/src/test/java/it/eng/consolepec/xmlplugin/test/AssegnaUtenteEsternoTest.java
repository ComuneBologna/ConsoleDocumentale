package it.eng.consolepec.xmlplugin.test;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.consolepec.xmlplugin.factory.DatiPratica.AssegnazioneEsterna;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Operazione;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.factory.PraticaFactory;
import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.factory.XMLPraticaFactory;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailIn;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Stato;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.XMLFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.TaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.ModificaAbilitazioniAssegnaUtenteEsternoTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.RitornaDaInoltrareEsternoTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;

public class AssegnaUtenteEsternoTest {

	Logger logger = LoggerFactory.getLogger(AssegnaUtenteEsternoTest.class);

	@Test
	public void testAssegnaUtenteEsterno() {

		Fascicolo fascicolo = TestUtils.compilaFascicoloGenericoDummy();

		TreeSet<String> destinatari = new TreeSet<String>(Arrays.asList("ilpizze@gmail.com"));
		TreeSet<Operazione> operazioni = new TreeSet<Operazione>(Arrays.asList(new Operazione(TipoApiTask.AGGIUNGI_ALLEGATO.name(), true)));
		fascicolo.getDati().setAssegnazioneEsterna(new AssegnazioneEsterna(operazioni, destinatari, "Testo Email", new Date()));

		XMLPraticaFactory xpf = new XMLPraticaFactory();
		StringWriter sw1 = new StringWriter();
		xpf.serializePraticaInstance(sw1, fascicolo);
		logger.debug("Originale serializzato {}", sw1.toString());
		Fascicolo fascicolo2 = xpf.loadPratica(Fascicolo.class, new StringReader(sw1.toString()));
		StringWriter sw0 = new StringWriter();
		xpf.serializePraticaInstance(sw0, fascicolo2);
		logger.debug("Copia deserializzata riserializzata: {}", sw0.toString());

		Assert.assertEquals(sw1.toString(), sw0.toString());
		Assert.assertEquals(fascicolo2.getDati().getTipo().getNomeTipologia(), "FASCICOLO_GENERICO");
		Assert.assertEquals(fascicolo2.getDati().getTipo().getNomeTipologia(), "FASCICOLO_GENERICO");
		Assert.assertEquals(fascicolo, fascicolo2);

		logger.debug(fascicolo.getDati().toString());
		logger.debug(fascicolo2.getDati().toString());
		Assert.assertEquals(fascicolo.getDati(), fascicolo2.getDati());
		StringWriter sw2 = new StringWriter();
		xpf.serializePraticaInstance(sw2, fascicolo2);
		Assert.assertEquals(sw1.toString(), sw2.toString());

		TaskFascicolo<?> task = getTaskCorrente(fascicolo2);
		task.setCurrentUser("CiaoCiao");
		task.setOperazioniAbilitate(TestUtils.getOperazioniAbilitate());
		((XMLFascicolo) fascicolo2).getDatiPraticaTaskAdapter().setStato(Stato.DA_INOLTRARE_ESTERNO);

		boolean controllaAbilitazione = task.controllaAbilitazione(TipoApiTask.MODIFICA_ABILITAZIONI_ASSEGNA_UTENTE_ESTERNO);
		Assert.assertTrue("Non Abilitato", controllaAbilitazione);

		TreeSet<String> ops = new TreeSet<String>(Arrays.asList(TipoApiTask.FIRMA.name(), TipoApiTask.ELIMINA_COLLEGAMENTO_FASCICOLO.name(), TipoApiTask.AGGIUNGI_ALLEGATO.name()));

		((ModificaAbilitazioniAssegnaUtenteEsternoTaskApi) task).modificaAbilitazioni(ops);

		Assert.assertEquals(fascicolo2.getDati().getAssegnazioneEsterna().getOperazioniConsentite().size(), 3);

		controllaAbilitazione = task.controllaAbilitazione(TipoApiTask.RITORNA_DA_INOLTRARE_ESTERNO);
		Assert.assertTrue("Non Abilitato", controllaAbilitazione);

		((RitornaDaInoltrareEsternoTaskApi) task).ritornaDaInoltrareEsterno(new ArrayList<Pratica<?>>());

		Assert.assertTrue("Stato diverso da in gestione", fascicolo2.getDati().getStato() == Stato.IN_GESTIONE);

	}

	@Test
	public void testAssegnaUtenteEsternoEmail() {
		PraticaFactory pf = new XMLPraticaFactory();
		final PraticaEmailIn mail = pf.loadPratica(PraticaEmailIn.class, TestUtils.getPraticaPECStream());

		TreeSet<String> destinatari = new TreeSet<String>(Arrays.asList("ilpizze@gmail.com"));
		TreeSet<Operazione> operazioni = new TreeSet<Operazione>(Arrays.asList(new Operazione(TipoApiTask.AGGIUNGI_ALLEGATO.name(), true)));
		mail.getDati().setAssegnazioneEsterna(new AssegnazioneEsterna(operazioni, destinatari, "Testo Email", new Date()));

		StringWriter sw1 = new StringWriter();
		pf.serializePraticaInstance(sw1, mail);
		logger.debug("Originale serializzato filippo: {}", sw1.toString());
		PraticaEmailIn mail2 = pf.loadPratica(PraticaEmailIn.class, new StringReader(sw1.toString()));

		StringWriter sw0 = new StringWriter();
		pf.serializePraticaInstance(sw0, mail2);
		logger.debug("Copia deserializzata riserializzata filippo: {}", sw0.toString());

		Assert.assertEquals(sw1.toString(), sw0.toString());

	}

	private TaskFascicolo<?> getTaskCorrente(Fascicolo fascicolo) {
		Set<Task<?>> tasks = fascicolo.getTasks();
		for (Task<?> t : tasks) {
			if (t instanceof TaskFascicolo && t.isAttivo()) {
				return (TaskFascicolo<?>) t;
			}
		}
		return null;
	}

}
