package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import it.eng.cobo.consolepec.commons.pratica.fascicolo.collegamenti.Permessi;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Operazione;
import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Collegamento;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Stato;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.TaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask.Condivisione;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.CollegaFascicoloTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.XMLApprovazioneFirmaTask;

public class CollegaFascicoloTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements CollegaFascicoloTaskApi {

	public CollegaFascicoloTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return getDatiFascicolo().getStato().equals(Stato.IN_GESTIONE) || getDatiFascicolo().getStato().equals(Stato.IN_VISIONE) || getDatiFascicolo().getStato().equals(Stato.DA_INOLTRARE_ESTERNO);
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.COLLEGA_FASCICOLO;
	}

	@Override
	public void collegaFascicolo(Fascicolo fascicoloRemoto, List<String> ruoliUtente, List<String> operazioniRemotePerUtenteLocale) throws PraticaException {
		if (isCollegamentoEsistente(fascicoloRemoto)) {
			modificaCollegamento(fascicoloRemoto, operazioniRemotePerUtenteLocale);
		} else {
			Permessi p = new Permessi(true, true, operazioniRemotePerUtenteLocale);
			creaNuovoCollegamento(fascicoloRemoto, ruoliUtente, p);
		}
	}

	@Override
	public void collegaFascicolo(Fascicolo fascicoloRemoto, List<String> ruoliUtente, Permessi permessi) throws PraticaException {
		if (isCollegamentoEsistente(fascicoloRemoto)) {
			modificaCollegamento(fascicoloRemoto, permessi);
		} else {
			creaNuovoCollegamento(fascicoloRemoto, ruoliUtente, permessi);
		}
	}

	private void creaNuovoCollegamento(Fascicolo fascicoloRemoto, List<String> ruoliUtente, Permessi permessi) {
		/* creo il collegamento in entrata verso di me */
		Condivisione condivisioniDestinatario = getCondivisioniDestinatario(ruoliUtente, fascicoloRemoto);
		String alfrescoPathLocale = task.getEnclosingPratica().getAlfrescoPath();
		Collegamento collegamentoRemoto = //
				new Collegamento(task.getDati().getAssegnatario().getNome(), alfrescoPathLocale, new Date(), permessi.isSorgenteAccessibileInLettura(), condivisioniDestinatario.getOperazioni());
		fascicoloRemoto.getDati().getCollegamenti().add(collegamentoRemoto);

		String assegnatarioFascicoloRemoto = extractTaskQualunque(fascicoloRemoto).getDati().getAssegnatario().getNome();
		TreeSet<Operazione> operazioniRemotePerUtenteLocaleBean = new TreeSet<Operazione>();
		for (String operazione : permessi.getOperazioniConsentite()) {
			operazioniRemotePerUtenteLocaleBean.add(new Operazione(operazione, true));
		}
		TreeSet<Collegamento> collegamenti = getDatiFascicolo().getCollegamenti();
		TreeSet<Collegamento> copyCollegamento = new TreeSet<Collegamento>();
		for (Collegamento coll : collegamenti) {
			if (!coll.getPath().equals(fascicoloRemoto.getAlfrescoPath())) {
				copyCollegamento.add(coll);
			}
		}
		Collegamento collegamentoSorgente = //
				new Collegamento(assegnatarioFascicoloRemoto, fascicoloRemoto.getAlfrescoPath(), new Date(), permessi.isRemotoAccessibileInLettura(), operazioniRemotePerUtenteLocaleBean);
		copyCollegamento.add(collegamentoSorgente);

		getDatiFascicolo().getCollegamenti().clear();
		getDatiFascicolo().getCollegamenti().addAll(copyCollegamento);

		generaEvento(EventiIterFascicolo.COLLEGA_FASCICOLO, task.getCurrentUser(), fascicoloRemoto.getDati().getIdDocumentale());
	}

	private void modificaCollegamento(Fascicolo fascicoloRemoto, List<String> operazioniRemotePerUtenteLocale) {
		String alfrescoPathRemoto = fascicoloRemoto.getAlfrescoPath();

		Collegamento collegamento = null;
		for (Collegamento c : getDatiFascicolo().getCollegamenti()) {
			if (c.getPath().equals(alfrescoPathRemoto)) {
				collegamento = c;
			}
		}

		assert collegamento != null : "Non esistono collegamenti da modificare.";

		collegamento.getOperazioniConsentite().clear();
		for (String operazione : operazioniRemotePerUtenteLocale) {
			collegamento.getOperazioniConsentite().add(new Operazione(operazione, true));
		}
	}

	// recupero la condivisione
	private static Condivisione getCondivisioniDestinatario(List<String> ruoliUtente, Fascicolo fascicoloDestinatario) {
		TaskFascicolo<?> taskFascicolo = extractTaskGestione(fascicoloDestinatario);

		TreeSet<Condivisione> condivisioni = ((DatiGestioneFascicoloTask) taskFascicolo.getDati()).getCondivisioni();

		for (Condivisione condivisione : condivisioni) {

			if (ruoliUtente.contains(condivisione.getNomeGruppo())) {
				return condivisione;
			}

		}
		throw new PraticaException("Non ci sono condivisioni per i gruppi dell'utente");
	}

	private static TaskFascicolo<?> extractTaskGestione(Fascicolo fascicoloDestinatario) {
		TaskFascicolo<?> taskFascicolo = null;

		Set<Task<?>> tasks = fascicoloDestinatario.getTasks();

		for (Task<?> t : tasks) {
			if (t instanceof TaskFascicolo) {
				taskFascicolo = (TaskFascicolo<?>) t;
			}
		}

		if (taskFascicolo == null) {
			throw new PraticaException("Il fascicolo non è attivo");
		}
		return taskFascicolo;
	}

	private static Task<?> extractTaskQualunque(Fascicolo fascicoloDestinatario) {
		Task<?> taskFascicolo = null;

		Set<Task<?>> tasks = fascicoloDestinatario.getTasks();

		for (Task<?> t : tasks) {
			if (t.isAttivo() && !(t instanceof XMLApprovazioneFirmaTask)) {
				taskFascicolo = t;
			}
		}

		if (taskFascicolo == null) {
			throw new PraticaException("Il fascicolo non ha nessuna operazione attiva");
		}
		return taskFascicolo;
	}

	public boolean isCollegamentoEsistente(Fascicolo fascicoloRemoto) {
		for (Collegamento c : fascicoloRemoto.getDati().getCollegamenti()) {
			if (c.getPath().equals(task.getEnclosingPratica().getAlfrescoPath())) {
				return true;
			}
		}
		return false;
	}

	private void modificaCollegamento(Fascicolo fascicoloRemoto, Permessi permessi) {
		// Modifica su sorgente
		Collegamento collegamentoSorgente = loadCollegamento((Fascicolo) task.getEnclosingPratica(), fascicoloRemoto.getAlfrescoPath());
		collegamentoSorgente.setAccessibileInLettura(permessi.isRemotoAccessibileInLettura());

		collegamentoSorgente.getOperazioniConsentite().clear();
		for (String operazione : permessi.getOperazioniConsentite()) {
			collegamentoSorgente.getOperazioniConsentite().add(new Operazione(operazione, true));
		}

		// Modifica su remoto
		Collegamento collegamentoRemoto = loadCollegamento(fascicoloRemoto, task.getEnclosingPratica().getAlfrescoPath());
		collegamentoRemoto.setAccessibileInLettura(permessi.isSorgenteAccessibileInLettura());
	}

	private static Collegamento loadCollegamento(Fascicolo fascicolo, String pathRemoto) {
		for (Collegamento c : fascicolo.getDati().getCollegamenti()) {
			if (c.getPath().equals(pathRemoto)) {
				return c;
			}
		}
		throw new PraticaException("Non esiste un collegamento verso il fascicolo: [" + pathRemoto + "] che parta da: [" + fascicolo.getAlfrescoPath() + "]");
	}

}
