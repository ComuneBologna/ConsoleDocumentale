package it.eng.consolepec.xmlplugin.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Operazione;
import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.pratica.albopretorio.FascicoloAlboPretorio;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Collegamento;
import it.eng.consolepec.xmlplugin.pratica.riservato.FascicoloRiservato;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.TaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask.Condivisione;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.CollegaFascicoloTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.CondividiFascicoloTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.EliminaCollegamentoFascicoloTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.EliminaCondivisioneTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;

public class GestioneCondivisioneECollegamentoFascicoliTest {

	@Test
	public void creazioneCondivisione() throws Exception {

		internalCreazioneCondivisione(getGruppoDiProva(0), 1);
	}

	private FascicoloAlboPretorio internalCreazioneCondivisione(AnagraficaRuolo gruppo, int totaleCondivisioni) {
		return internalCreazioneCondivisione(gruppo, totaleCondivisioni, null);

	}

	private FascicoloAlboPretorio internalCreazioneCondivisione(AnagraficaRuolo gruppo, int totaleCondivisioni, FascicoloAlboPretorio fascicoloAlboPretorioIN) {
		FascicoloAlboPretorio fascicoloAlboPretorio = fascicoloAlboPretorioIN;
		if (fascicoloAlboPretorio == null)
			fascicoloAlboPretorio = TestUtils.compilaFascicoloAlboPretorioDummy();

		// recupero task gestione fascicolo albo pretorio
		TaskFascicolo<?> taskFascicolo = null;
		Set<Task<?>> tasks = fascicoloAlboPretorio.getTasks();
		for (Task<?> t : tasks) {
			if (t instanceof TaskFascicolo && t.isAttivo())
				taskFascicolo = (TaskFascicolo<?>) t;
		}

		Assert.assertTrue(taskFascicolo.controllaAbilitazione(TipoApiTask.CONDIVIDI_FASCICOLO));

		List<String> operazioni = Arrays.asList(TipoApiTask.AGGIUNGI_ALLEGATO.name(), TipoApiTask.FIRMA.name(), TipoApiTask.RIMUOVI_ALLEGATO.name());

		((CondividiFascicoloTaskApi) taskFascicolo).condividi(gruppo, operazioni);

		TreeSet<Condivisione> condivisioni = ((DatiGestioneFascicoloTask) taskFascicolo.getDati()).getCondivisioni();

		Assert.assertEquals(condivisioni.size(), totaleCondivisioni);

		ArrayList<String> listaCondivisioni = new ArrayList<String>();
		for (Condivisione c : condivisioni) {
			listaCondivisioni.add(c.getNomeGruppo());
		}

		Assert.assertTrue(listaCondivisioni.contains(gruppo.getRuolo()));

		TreeSet<Operazione> operazioni2 = condivisioni.first().getOperazioni();
		List<String> operazioni3 = new ArrayList<String>();

		for (Operazione op : operazioni2) {
			operazioni3.add(op.getNomeOperazione());
		}

		Assert.assertEquals(operazioni, operazioni3);

		return fascicoloAlboPretorio;
	}

	@Test
	public void modificaCondivisione() throws Exception {

		FascicoloAlboPretorio fascicoloAlboPretorio = internalCreazioneCondivisione(getGruppoDiProva(0), 1);

		// recupero task gestione fascicolo albo pretorio
		TaskFascicolo<?> taskFascicolo = null;
		Set<Task<?>> tasks = fascicoloAlboPretorio.getTasks();
		for (Task<?> t : tasks) {
			if (t instanceof TaskFascicolo && t.isAttivo())
				taskFascicolo = (TaskFascicolo<?>) t;
		}

		List<String> operazioniDaAggiungere = Arrays.asList(TipoApiTask.METTI_IN_AFFISSIONE.name(), TipoApiTask.RISPONDI_MAIL.name());
		List<String> operazioniDaRimuovere = Arrays.asList(TipoApiTask.RIMUOVI_ALLEGATO.name());

		TreeSet<Condivisione> condivisioni = ((DatiGestioneFascicoloTask) taskFascicolo.getDati()).getCondivisioni();

		TreeSet<Operazione> operazioniCondivise = condivisioni.first().getOperazioni();
		String nomeGruppo = condivisioni.first().getNomeGruppo();

		List<String> nuovaListaOperazioni = new ArrayList<String>();

		nuovaListaOperazioni.addAll(operazioniDaAggiungere);

		for (String op : operazioniDaRimuovere) {
			for (Operazione operazione : operazioniCondivise) {
				if (!operazione.getNomeOperazione().equals(op))
					nuovaListaOperazioni.add(operazione.getNomeOperazione());
			}
		}

		Assert.assertTrue(taskFascicolo.controllaAbilitazione(TipoApiTask.CONDIVIDI_FASCICOLO));

		AnagraficaRuolo ar = new AnagraficaRuolo();
		ar.setRuolo(nomeGruppo);
		((CondividiFascicoloTaskApi) taskFascicolo).condividi(ar, nuovaListaOperazioni);

		condivisioni = ((DatiGestioneFascicoloTask) taskFascicolo.getDati()).getCondivisioni();

		Assert.assertEquals(condivisioni.size(), 1);

		Assert.assertEquals(condivisioni.first().getNomeGruppo(), getGruppoDiProva(0).getRuolo());

		TreeSet<Operazione> operazioni2 = condivisioni.first().getOperazioni();
		List<String> operazioni3 = new ArrayList<String>();

		for (Operazione op : operazioni2) {
			operazioni3.add(op.getNomeOperazione());
		}

		Assert.assertEquals(new TreeSet<String>(operazioni3), new TreeSet<String>(nuovaListaOperazioni));

	}

	@Test
	public void eliminazioneCondivisione() {

		FascicoloAlboPretorio fascicoloAlboPretorio = internalCreazioneCondivisione(getGruppoDiProva(1), 1);
		fascicoloAlboPretorio = internalCreazioneCondivisione(getGruppoDiProva(2), 2, fascicoloAlboPretorio);
		fascicoloAlboPretorio = internalCreazioneCondivisione(getGruppoDiProva(3), 3, fascicoloAlboPretorio);
		fascicoloAlboPretorio = internalCreazioneCondivisione(getGruppoDiProva(4), 4, fascicoloAlboPretorio);
		fascicoloAlboPretorio = internalCreazioneCondivisione(getGruppoDiProva(5), 5, fascicoloAlboPretorio);

		// recupero task gestione fascicolo albo pretorio
		TaskFascicolo<?> taskFascicolo = null;
		Set<Task<?>> tasks = fascicoloAlboPretorio.getTasks();
		for (Task<?> t : tasks) {
			if (t instanceof TaskFascicolo && t.isAttivo())
				taskFascicolo = (TaskFascicolo<?>) t;
		}

		Assert.assertTrue(taskFascicolo.controllaAbilitazione(TipoApiTask.ELIMINA_CONDIVISIONE_FASCICOLO));

		((EliminaCondivisioneTaskApi) taskFascicolo).eliminaCondivisione(getGruppoDiProva(2));

		((EliminaCondivisioneTaskApi) taskFascicolo).eliminaCondivisione(getGruppoDiProva(4));

		TreeSet<Condivisione> condivisioni = ((DatiGestioneFascicoloTask) taskFascicolo.getDati()).getCondivisioni();

		Assert.assertEquals(condivisioni.size(), 3);

		TreeSet<String> elencocondivisioni = new TreeSet<String>();

		for (Condivisione condivisione : condivisioni) {
			elencocondivisioni.add(condivisione.getNomeGruppo());
		}

		Assert.assertTrue(elencocondivisioni.contains(getGruppoDiProva(1).getRuolo()));
		Assert.assertTrue(elencocondivisioni.contains(getGruppoDiProva(3).getRuolo()));
		Assert.assertTrue(elencocondivisioni.contains(getGruppoDiProva(5).getRuolo()));

		Assert.assertFalse(elencocondivisioni.contains(getGruppoDiProva(2).getRuolo()));
		Assert.assertFalse(elencocondivisioni.contains(getGruppoDiProva(4).getRuolo()));

	}

	@Test
	public void creazioneCollegamento() {

		FascicoloAlboPretorio fascicoloAlboPretorio = internalCreazioneCondivisione(getGruppoDiProvaRiscossione(), 1);
		FascicoloRiservato fascicoloEntrateRiscossione = TestUtils.compilaFascicoloRiservatoDummy();

		// recupero task gestione fascicolo riscossione
		TaskFascicolo<?> taskFascicolo = null;
		Set<Task<?>> tasks = fascicoloEntrateRiscossione.getTasks();
		for (Task<?> t : tasks) {
			if (t instanceof TaskFascicolo && t.isAttivo())
				taskFascicolo = (TaskFascicolo<?>) t;
		}

		Assert.assertTrue(taskFascicolo.controllaAbilitazione(TipoApiTask.COLLEGA_FASCICOLO));

		List<String> operazioni = Arrays.asList(TipoApiTask.METTI_IN_AFFISSIONE.name(), TipoApiTask.ELIMINA_CONDIVISIONE_FASCICOLO.name(), TipoApiTask.RISPONDI_MAIL.name());

		((CollegaFascicoloTaskApi) taskFascicolo).collegaFascicolo(fascicoloAlboPretorio, Arrays.asList(taskFascicolo.getDati().getAssegnatario().getNome()), operazioni);

		TreeSet<Collegamento> collegamentiDestinatario = fascicoloAlboPretorio.getDati().getCollegamenti();

		TreeSet<String> elencoOperazioni = new TreeSet<String>();
		for (Operazione operazione : collegamentiDestinatario.first().getOperazioniConsentite()) {
			elencoOperazioni.add(operazione.getNomeOperazione());
		}

		Assert.assertEquals(collegamentiDestinatario.size(), 1);
		Assert.assertEquals(collegamentiDestinatario.first().getNomeGruppo(), getGruppoDiProvaRiscossione().getRuolo());
		// Assert.assertEquals(new TreeSet<String>(operazioni), elencoOperazioni);

		TreeSet<Collegamento> collegamentiSorgente = fascicoloEntrateRiscossione.getDati().getCollegamenti();

		TreeSet<String> elencoOperazioni2 = new TreeSet<String>();
		for (Operazione operazione : collegamentiSorgente.first().getOperazioniConsentite()) {
			elencoOperazioni2.add(operazione.getNomeOperazione());
		}

		Assert.assertEquals(collegamentiSorgente.size(), 1);
		Assert.assertEquals(collegamentiSorgente.first().getNomeGruppo(), getGruppoDiProvaAlboPretorio().getRuolo());

		// recupero task gestione fascicolo albo pretorio
		TaskFascicolo<?> taskFascicoloDestinatario = null;
		tasks = fascicoloAlboPretorio.getTasks();
		for (Task<?> t : tasks) {
			if (t instanceof TaskFascicolo && t.isAttivo())
				taskFascicoloDestinatario = (TaskFascicolo<?>) t;
		}

		TreeSet<String> elencoOperazioni3 = new TreeSet<String>();
		TreeSet<Condivisione> condivisioni = ((DatiGestioneFascicoloTask) taskFascicoloDestinatario.getDati()).getCondivisioni();

		for (Condivisione condivisione : condivisioni) {
			if (condivisione.getNomeGruppo().equalsIgnoreCase(getGruppoDiProva(1).getRuolo())) {
				TreeSet<Operazione> operazioni2 = condivisione.getOperazioni();
				for (Operazione operazione : operazioni2)
					elencoOperazioni3.add(operazione.getNomeOperazione());
			}
		}

		// Assert.assertEquals(elencoOperazioni3, elencoOperazioni2);

	}

	@Test
	public void eliminazioneCollegamento() {

		FascicoloAlboPretorio fascicoloAlboPretorio = internalCreazioneCondivisione(getGruppoDiProvaRiscossione(), 1);
		FascicoloRiservato fascicoloEntrateRiscossione = TestUtils.compilaFascicoloRiservatoDummy();

		// recupero task gestione fascicolo riscossione
		TaskFascicolo<?> taskFascicolo = null;
		Set<Task<?>> tasks = fascicoloEntrateRiscossione.getTasks();
		for (Task<?> t : tasks) {
			if (t instanceof TaskFascicolo && t.isAttivo())
				taskFascicolo = (TaskFascicolo<?>) t;
		}

		Assert.assertTrue(taskFascicolo.controllaAbilitazione(TipoApiTask.COLLEGA_FASCICOLO));

		List<String> operazioni = Arrays.asList(TipoApiTask.METTI_IN_AFFISSIONE.name(), TipoApiTask.ELIMINA_CONDIVISIONE_FASCICOLO.name(), TipoApiTask.RISPONDI_MAIL.name());

		((CollegaFascicoloTaskApi) taskFascicolo).collegaFascicolo(fascicoloAlboPretorio, Arrays.asList(taskFascicolo.getDati().getAssegnatario().getNome()), operazioni);

		TreeSet<Collegamento> collegamentiDestinatario = fascicoloAlboPretorio.getDati().getCollegamenti();

		TreeSet<String> elencoOperazioni = new TreeSet<String>();
		for (Operazione operazione : collegamentiDestinatario.first().getOperazioniConsentite()) {
			elencoOperazioni.add(operazione.getNomeOperazione());
		}

		Assert.assertEquals(collegamentiDestinatario.size(), 1);
		Assert.assertEquals(collegamentiDestinatario.first().getNomeGruppo(), getGruppoDiProvaRiscossione().getRuolo());
		// Assert.assertEquals(new TreeSet<String>(operazioni), elencoOperazioni);

		TreeSet<Collegamento> collegamentiSorgente = fascicoloEntrateRiscossione.getDati().getCollegamenti();

		TreeSet<String> elencoOperazioni2 = new TreeSet<String>();
		for (Operazione operazione : collegamentiSorgente.first().getOperazioniConsentite()) {
			elencoOperazioni2.add(operazione.getNomeOperazione());
		}

		Assert.assertEquals(collegamentiSorgente.size(), 1);
		Assert.assertEquals(collegamentiSorgente.first().getNomeGruppo(), getGruppoDiProvaAlboPretorio().getRuolo());

		// recupero task gestione fascicolo albo pretorio
		TaskFascicolo<?> taskFascicoloDestinatario = null;
		tasks = fascicoloAlboPretorio.getTasks();
		for (Task<?> t : tasks) {
			if (t instanceof TaskFascicolo && t.isAttivo())
				taskFascicoloDestinatario = (TaskFascicolo<?>) t;
		}

		TreeSet<String> elencoOperazioni3 = new TreeSet<String>();
		TreeSet<Condivisione> condivisioni = ((DatiGestioneFascicoloTask) taskFascicoloDestinatario.getDati()).getCondivisioni();

		for (Condivisione condivisione : condivisioni) {
			if (condivisione.getNomeGruppo().equalsIgnoreCase(getGruppoDiProva(1).getRuolo())) {
				TreeSet<Operazione> operazioni2 = condivisione.getOperazioni();
				for (Operazione operazione : operazioni2)
					elencoOperazioni3.add(operazione.getNomeOperazione());
			}
		}

		// Assert.assertEquals(elencoOperazioni3, elencoOperazioni2);

		Assert.assertTrue(taskFascicolo.controllaAbilitazione(TipoApiTask.ELIMINA_COLLEGAMENTO_FASCICOLO));

		((EliminaCollegamentoFascicoloTaskApi) taskFascicolo).eliminaCollegamento(fascicoloAlboPretorio);

		Assert.assertTrue(fascicoloEntrateRiscossione.getDati().getCollegamenti().isEmpty());
		Assert.assertTrue(fascicoloAlboPretorio.getDati().getCollegamenti().isEmpty());

	}

	@Test
	public void gestioneTaskPraticheCondiviseCollegate() {

		FascicoloAlboPretorio fascicoloAlboPretorio = internalCreazioneCondivisione(getGruppoDiProva(1), 1);

		TaskFascicolo<?> taskFascicoloDestinatario = null;
		Set<Task<?>> tasks = fascicoloAlboPretorio.getTasks();
		for (Task<?> t : tasks) {
			if (t instanceof TaskFascicolo && t.isAttivo() && t.getDati().getAssegnatario().getNome().equals(getGruppoDiProva(1).getRuolo()))
				taskFascicoloDestinatario = (TaskFascicolo<?>) t;
		}

		Assert.assertNull(taskFascicoloDestinatario);

		tasks = fascicoloAlboPretorio.getTasks();
		for (Task<?> t : tasks) {
			if (t instanceof TaskFascicolo && t.isAttivo() && !t.getDati().getAssegnatario().getNome().equals(getGruppoDiProva(1).getRuolo())) {
				TaskFascicolo<?> temp = (TaskFascicolo<?>) t;
				TreeSet<Condivisione> condivisioni = ((DatiGestioneFascicoloTask) temp.getDati()).getCondivisioni();
				for (Condivisione condivisione : condivisioni)
					if (condivisione.getNomeGruppo().equals(getGruppoDiProva(1).getRuolo()))
						taskFascicoloDestinatario = temp;
			}
		}
		Assert.assertNotNull(taskFascicoloDestinatario);
	}

	private AnagraficaRuolo getGruppoDiProva(int n) {
		AnagraficaRuolo ar = new AnagraficaRuolo();

		String ruolo = "DOC_GRUPPO_DI_PROVA";
		if (n > 0)
			ruolo = ruolo + "_" + n;

		String etichetta = "GRUPPO DI PROVA";
		if (n > 0)
			etichetta = etichetta + " " + n;

		ar.setRuolo(ruolo);
		ar.setEtichetta(etichetta);
		return ar;
	}

	private AnagraficaRuolo getGruppoDiProvaRiscossione() {
		AnagraficaRuolo ar = new AnagraficaRuolo();

		String ruolo = "Ruolo di prova per riscossione";
		String etichetta = "Riscossione";

		ar.setRuolo(ruolo);
		ar.setEtichetta(etichetta);
		return ar;
	}

	private AnagraficaRuolo getGruppoDiProvaAlboPretorio() {
		AnagraficaRuolo ar = new AnagraficaRuolo();

		String ruolo = "Ruolo di prova per albo pretorio";
		String etichetta = "Albo";

		ar.setRuolo(ruolo);
		ar.setEtichetta(etichetta);
		return ar;
	}
}
