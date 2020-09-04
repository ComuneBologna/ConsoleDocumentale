package it.eng.consolepec.xmlplugin.test;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.GruppoVisibilita;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.TipoFirma;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.factory.TaskObserver.AggiungiAllegato;
import it.eng.consolepec.xmlplugin.factory.XMLPraticaFactory;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.RichiestaApprovazioneFirmaTaskApi.RichiestaApprovazioneFirmaBean;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.DestinatarioGruppoRichiestaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.DestinatarioRichiestaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.DestinatarioUtenteRichiestaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.StatoDestinatarioRichiestaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.StatoRichiestaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.TipoPropostaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.TipoRispostaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.XMLApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.util.TaskDiFirmaUtil;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

public class TestApprovazioneFirmaTask {

	private static Logger logger = LoggerFactory.getLogger(TemplateTest.class);
	private static final String utenteCorrente = "Ataulfo";
	private static final String ruoloCorrente = "DOC_0000_BimBumBamAleLoGiuro";

	@Test
	public void test() throws Exception {

		logger.info("TEST TASK DI FIRMA");

		Fascicolo fascicolo = TestUtils.compilaFascicoloGenericoDummy();
		Allegato a1 = TestUtils.buildAllegato(fascicolo.getDati(), "nomeAllegato1", "labelAllegato1", "ALLEGATI", 234L, "1.0", true, TipoFirma.CADES, false, null, null, null);
		fascicolo.getDati().getAllegati().add(a1);
		Allegato a2 = TestUtils.buildAllegato(fascicolo.getDati(), "nomeAllegato2", "labelAllegato2", "ALLEGATI", 234L, "1.0", true, TipoFirma.CADES, false, null, null, null);
		fascicolo.getDati().getAllegati().add(a2);
		Allegato a3 = TestUtils.buildAllegato(fascicolo.getDati(), "nomeAllegato3", "labelAllegato3", "ALLEGATI", 234L, "1.0", true, TipoFirma.CADES, false, null, null, null);
		fascicolo.getDati().getAllegati().add(a3);
		Allegato a4 = TestUtils.buildAllegato(fascicolo.getDati(), "nomeAllegato4", "labelAllegato4", "ALLEGATI", 234L, "1.0", true, TipoFirma.CADES, false, null, null, null);
		fascicolo.getDati().getAllegati().add(a3);

		List<String> destinatariNotifica = Arrays.asList(new String[] { "dest1@test.eng.it", "dest2@test.eng.it" });

		task1(fascicolo, a1, getDestinatariRichiestaFirma(), destinatariNotifica);
		task2(fascicolo, a2, getDestinatariRichiestaFirma(), destinatariNotifica);
		task3(fascicolo, a3, getDestinatariRichiestaFirma(), destinatariNotifica);
		task4(fascicolo, a4, getDestinatariGruppiRichiestaFirma(), destinatariNotifica);
	}

	private TreeSet<DestinatarioRichiestaApprovazioneFirmaTask> getDestinatariRichiestaFirma() {
		TreeSet<DestinatarioRichiestaApprovazioneFirmaTask> d = new TreeSet<DestinatarioRichiestaApprovazioneFirmaTask>();
		DestinatarioUtenteRichiestaApprovazioneFirmaTask dest1 = new DestinatarioUtenteRichiestaApprovazioneFirmaTask("ggaribaldi", "Giuseppe", "Garibaldi", "XXXXXXXX", "settore1",
				StatoDestinatarioRichiestaApprovazioneFirmaTask.IN_APPROVAZIONE);
		DestinatarioUtenteRichiestaApprovazioneFirmaTask dest2 = new DestinatarioUtenteRichiestaApprovazioneFirmaTask("gmazzini", "Giuseppe", "Mazzini", "XXXXXXXX", "settore1",
				StatoDestinatarioRichiestaApprovazioneFirmaTask.IN_APPROVAZIONE);
		d.add(dest1);
		d.add(dest2);
		return d;
	}

	private TreeSet<DestinatarioRichiestaApprovazioneFirmaTask> getDestinatariGruppiRichiestaFirma() {
		TreeSet<DestinatarioRichiestaApprovazioneFirmaTask> d = new TreeSet<DestinatarioRichiestaApprovazioneFirmaTask>();
		DestinatarioGruppoRichiestaApprovazioneFirmaTask dest1 = new DestinatarioGruppoRichiestaApprovazioneFirmaTask("DOC_CIPPIRIMERLO",
				StatoDestinatarioRichiestaApprovazioneFirmaTask.IN_APPROVAZIONE);
		d.add(dest1);
		return d;
	}

	private XMLApprovazioneFirmaTask addTaskApprovazioneFirma(Fascicolo fascicolo, Allegato a, TipoPropostaApprovazioneFirmaTask tipo,
			TreeSet<DestinatarioRichiestaApprovazioneFirmaTask> destinatariRichiestaFirma, List<String> destinatariNotifica) {
		RichiestaApprovazioneFirmaBean b1 = new RichiestaApprovazioneFirmaBean();
		b1.setAllegato(a);
		b1.setDestinatari(destinatariRichiestaFirma);
		b1.setTipoRichiestaFirma(tipo);
		b1.setGruppoProponente(ruoloCorrente);
		b1.setOggettoDocumento("Hai detto Sandro?");
		XMLTaskFascicolo<DatiGestioneFascicoloTask> gestioneFasicoloTask = XmlPluginUtil.getGestioneFasicoloTaskCorrente(fascicolo, utenteCorrente);
		gestioneFasicoloTask.inviaInApprovazione(b1, destinatariNotifica, "NOTE", "Sandro");
		XMLApprovazioneFirmaTask task = TaskDiFirmaUtil.getApprovazioneFirmaTaskAttivoByAllegato(fascicolo, a);
		Assert.assertTrue(a.getLock());
		Assert.assertNotNull(a.getLockedBy());
		Assert.assertTrue(a.getLockedBy().equals(task.getDati().getId()));
		testSerializzazioneConTaskDiFirma(fascicolo);
		return task;
	}

	/*
	 * TEST CON RICHIESTA FIRMA, APPROVAZIONE (DELLA FIRMA) E RIASSEGNAZIONE
	 */
	private void task1(Fascicolo fascicolo, Allegato a, TreeSet<DestinatarioRichiestaApprovazioneFirmaTask> destinatariRichiestaFirma, List<String> destinatariNotifica) throws Exception {
		XMLTaskFascicolo<?> taskFascicolo = XmlPluginUtil.getGestioneTaskCorrente(fascicolo, XMLTaskFascicolo.class, utenteCorrente);
		Allegato a1 = versionaAllegato(a, fascicolo, "2.0", taskFascicolo);
		XMLApprovazioneFirmaTask task = addTaskApprovazioneFirma(fascicolo, a1, TipoPropostaApprovazioneFirmaTask.FIRMA, destinatariRichiestaFirma, destinatariNotifica);
		String note1 = "note aaaaaaaaaaaaa";
		Allegato a2 = versionaAllegato(a1, fascicolo, "3.0", task);
		task.firmaAllegati(Arrays.asList(new Allegato[] { a2 }));
		DestinatarioUtenteRichiestaApprovazioneFirmaTask first = (DestinatarioUtenteRichiestaApprovazioneFirmaTask) destinatariRichiestaFirma.first();
		DestinatarioUtenteRichiestaApprovazioneFirmaTask last = (DestinatarioUtenteRichiestaApprovazioneFirmaTask) destinatariRichiestaFirma.last();
		task.approva(a2, first.getNomeUtente(), "DOC_000_Sandro", note1, Arrays.asList(new String[] { "biagio.tozzi@gmail.com" }), null);
		Assert.assertTrue(StatoRichiestaApprovazioneFirmaTask.IN_APPROVAZIONE.name().equals(task.getDati().getStato()));
		String note2 = "note bbbbbbbbbbbbb";
		Allegato a3 = versionaAllegato(a2, fascicolo, "4.0", task);
		task.firmaAllegati(Arrays.asList(new Allegato[] { a3 }));
		task.approva(a3, last.getNomeUtente(), "DOC_000_Sandro", note2, Arrays.asList(new String[] { "biagio.tozzi@gmail.com" }), null);
		AnagraficaRuolo ar = new AnagraficaRuolo();
		ar.setRuolo("DOC_00000_GRUPPO DUMMY");
		task.riassegna(ar, new ArrayList<GruppoVisibilita>(), new ArrayList<Pratica<?>>(), "Operatore1", destinatariNotifica);
		Assert.assertTrue(StatoRichiestaApprovazioneFirmaTask.APPROVATO.name().equals(task.getDati().getStato()));
		Assert.assertFalse(task.getDati().getAttivo());
		Assert.assertFalse(a3.getLock());
		Assert.assertNull(a3.getLockedBy());
	}

	/*
	 * TEST CON RICHIESTA FIRMA, APPROVAZIONE E RITIRO
	 */
	private void task2(Fascicolo fascicolo, Allegato a, TreeSet<DestinatarioRichiestaApprovazioneFirmaTask> destinatariRichiestaFirma, List<String> destinatariNotifica) throws Exception {
		XMLTaskFascicolo<?> taskFascicolo = XmlPluginUtil.getGestioneTaskCorrente(fascicolo, XMLTaskFascicolo.class, utenteCorrente);
		Allegato a1 = versionaAllegato(a, fascicolo, "2.0", taskFascicolo);
		XMLApprovazioneFirmaTask task = addTaskApprovazioneFirma(fascicolo, a1, TipoPropostaApprovazioneFirmaTask.FIRMA, destinatariRichiestaFirma, destinatariNotifica);
		String note1 = "note ccccccccccccccccc";
		Allegato a2 = versionaAllegato(a1, fascicolo, "3.0", task);
		task.firmaAllegati(Arrays.asList(new Allegato[] { a2 }));
		DestinatarioUtenteRichiestaApprovazioneFirmaTask first = (DestinatarioUtenteRichiestaApprovazioneFirmaTask) destinatariRichiestaFirma.first();
		task.approva(a2, first.getNomeUtente(), "DOC_000_Sandro", note1, Arrays.asList(new String[] { "biagio.tozzi@gmail.com" }), null);
		Assert.assertTrue(StatoRichiestaApprovazioneFirmaTask.IN_APPROVAZIONE.name().equals(task.getDati().getStato()));
		Allegato a3 = versionaAllegato(a2, fascicolo, "4.0", task);
		taskFascicolo.ritira(a3, task, "frrr", destinatariNotifica, Arrays.asList(ruoloCorrente), "Sandro");
		Assert.assertTrue(StatoRichiestaApprovazioneFirmaTask.RITIRATO.name().equals(task.getDati().getStato()));
		Assert.assertFalse(task.getDati().getAttivo());
		Assert.assertFalse(a3.getLock());
		Assert.assertNull(a3.getLockedBy());
	}

	/*
	 * TEST CON RICHIESTA VISTO E DINIEGO
	 */
	private void task3(Fascicolo fascicolo, Allegato a, TreeSet<DestinatarioRichiestaApprovazioneFirmaTask> destinatariRichiestaFirma, List<String> destinatariNotifica) throws Exception {
		XMLTaskFascicolo<?> taskFascicolo = XmlPluginUtil.getGestioneTaskCorrente(fascicolo, XMLTaskFascicolo.class, utenteCorrente);
		Allegato a1 = versionaAllegato(a, fascicolo, "2.0", taskFascicolo);
		XMLApprovazioneFirmaTask task = addTaskApprovazioneFirma(fascicolo, a1, TipoPropostaApprovazioneFirmaTask.VISTO, destinatariRichiestaFirma, destinatariNotifica);
		String note1 = "note dddddddddddddddddd";
		Allegato a2 = versionaAllegato(a1, fascicolo, "3.0", task);
		DestinatarioUtenteRichiestaApprovazioneFirmaTask first = (DestinatarioUtenteRichiestaApprovazioneFirmaTask) destinatariRichiestaFirma.first();
		DestinatarioUtenteRichiestaApprovazioneFirmaTask last = (DestinatarioUtenteRichiestaApprovazioneFirmaTask) destinatariRichiestaFirma.last();
		task.approva(a2, first.getNomeUtente(), "DOC_000_Sandro", note1, Arrays.asList(new String[] { "biagio.tozzi@gmail.com" }), null);
		Assert.assertTrue(StatoRichiestaApprovazioneFirmaTask.IN_APPROVAZIONE.name().equals(task.getDati().getStato()));
		Allegato a3 = versionaAllegato(a2, fascicolo, "4.0", task);
		task.diniega(a3, last.getNomeUtente(), "DOC_000_Sandro", "Non va bene", Arrays.asList(new String[] { "biagio.tozzi@gmail.com" }), null);
		Assert.assertTrue(StatoRichiestaApprovazioneFirmaTask.DINIEGATO.name().equals(task.getDati().getStato()));
		Assert.assertFalse(task.getDati().getAttivo());
		Assert.assertFalse(a3.getLock());
		Assert.assertNull(a3.getLockedBy());
	}

	/*
	 * TEST CON RICHIESTA PARERE, EVASIONE TASK E INVALIDAZIONE
	 */
	private void task4(Fascicolo fascicolo, Allegato a, TreeSet<DestinatarioRichiestaApprovazioneFirmaTask> destinatariRichiestaFirma, List<String> destinatariNotifica) throws Exception {
		XMLTaskFascicolo<?> taskFascicolo = XmlPluginUtil.getGestioneTaskCorrente(fascicolo, XMLTaskFascicolo.class, utenteCorrente);
		Allegato a1 = versionaAllegato(a, fascicolo, "2.0", taskFascicolo);
		XMLApprovazioneFirmaTask task = addTaskApprovazioneFirma(fascicolo, a1, TipoPropostaApprovazioneFirmaTask.PARERE, destinatariRichiestaFirma, destinatariNotifica);
		String note1 = "note dddddddddddddddddd";
		Allegato a2 = versionaAllegato(a1, fascicolo, "3.0", task);
		DestinatarioGruppoRichiestaApprovazioneFirmaTask first = (DestinatarioGruppoRichiestaApprovazioneFirmaTask) destinatariRichiestaFirma.first();
		task.rispondi(TipoRispostaApprovazioneFirmaTask.RISPOSTA_POSITIVA, a2, "ASandro", first.getNomeGruppo(), note1, Arrays.asList(new String[] { "biagio.tozzi@gmail.com" }), null);
		Assert.assertTrue(StatoRichiestaApprovazioneFirmaTask.PARERE_RICEVUTO.name().equals(task.getDati().getStato()));
		Allegato a3 = versionaAllegato(a2, fascicolo, "4.0", task);
		task.evadi(Arrays.asList(ruoloCorrente));
		Assert.assertTrue(StatoRichiestaApprovazioneFirmaTask.EVASO.name().equals(task.getDati().getStato()));
		Assert.assertFalse(task.getDati().getAttivo());
		Assert.assertFalse(a3.getLock());
		Assert.assertNull(a3.getLockedBy());

		fascicolo.getDati().getAllegati().remove(a3);
		task.invalida();
		Assert.assertFalse(task.getDati().getValido());
	}

	private void testSerializzazioneConTaskDiFirma(Fascicolo fascicolo) {
		// prova serializzazione
		XMLPraticaFactory xpf = new XMLPraticaFactory();
		StringWriter sw1 = new StringWriter();
		xpf.serializePraticaInstance(sw1, fascicolo);
		// e deserializzazione
		StringReader sr1 = new StringReader(sw1.toString());
		Fascicolo fascicolo2 = xpf.loadPratica(Fascicolo.class, sr1);
		StringWriter sw2 = new StringWriter();
		xpf.serializePraticaInstance(sw2, fascicolo2);
		Assert.assertEquals(sw1.toString(), sw2.toString());
		Assert.assertEquals(fascicolo, fascicolo2);
	}

	private Allegato versionaAllegato(Allegato a, Fascicolo fascicolo, String versione, XMLApprovazioneFirmaTask task) throws Exception {
		final Allegato allegato = TestUtils.buildAllegato(fascicolo.getDati(), a.getNome(), a.getLabel(), "ALLEGATI", 234L, versione, a.getFirmato(), a.getTipoFirma(), a.getFirmatoHash(), null, null,
				null);

		if (a.getStoricoVersioni() != null) allegato.getStoricoVersioni().addAll(a.getStoricoVersioni());

		allegato.setLock(a.getLock());
		allegato.setLockedBy(a.getLockedBy());
		allegato.setOggettoDocumento(a.getOggettoDocumento());

		AggiungiAllegato handler = new AggiungiAllegato() {
			@Override
			public Allegato aggiungiAllegato(String alfrescoPathAllegato, String nomeAllegato) throws ApplicationException {
				return allegato;
			}
		};

		task.versionaAllegato(allegato, false, handler);
		return allegato;
	}

	private Allegato versionaAllegato(Allegato a, Fascicolo fascicolo, String versione, XMLTaskFascicolo<?> task) throws Exception {
		final Allegato allegato = TestUtils.buildAllegato(fascicolo.getDati(), a.getNome(), a.getLabel(), "ALLEGATI", 234L, versione, a.getFirmato(), a.getTipoFirma(), a.getFirmatoHash(), null, null,
				null);

		if (a.getStoricoVersioni() != null) allegato.getStoricoVersioni().addAll(a.getStoricoVersioni());

		allegato.setLock(a.getLock());
		allegato.setLockedBy(a.getLockedBy());
		allegato.setOggettoDocumento(a.getOggettoDocumento());

		AggiungiAllegato handler = new AggiungiAllegato() {
			@Override
			public Allegato aggiungiAllegato(String alfrescoPathAllegato, String nomeAllegato) throws ApplicationException {
				return allegato;
			}
		};

		task.versionaAllegato(allegato, false, handler);

		return allegato;
	}
}
