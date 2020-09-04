package it.eng.consolepec.xmlplugin.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import it.eng.cobo.consolepec.util.generics.GenericsUtil;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.StoricoVersioni;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.StoricoVersioni.InformazioniTaskFirma;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.ApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.DestinatarioGruppoRichiestaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.DestinatarioRichiestaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.DestinatarioUtenteRichiestaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.RiferimentoAllegatoApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.StatoDestinatarioRichiestaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.XMLApprovazioneFirmaTask;

public class TaskDiFirmaUtil {

	public static boolean riassegnazioneAbilitata(Map<Pratica<?>, List<Allegato>> praticaAllegatiMap, boolean isOperazioneConclusiva) {

		for (Entry<Pratica<?>, List<Allegato>> entry : praticaAllegatiMap.entrySet()) {
			Pratica<?> pratica = entry.getKey();
			List<Allegato> allegati = entry.getValue();

			List<Allegato> allegatiEsclusi = new ArrayList<Allegato>(pratica.getDati().getAllegati());
			allegatiEsclusi.removeAll(allegati);

			/*
			 * Se c'è almeno un allegato sul quale non voglio operare: 1. Se uno di questi è in un task di firma attivo e valido allora non è abilitata la riassegnazione nel task di firma. 2. Se
			 * nessuno di questi è in un task di firma attivo allora contollo quelli sui quali voglio operare.
			 *
			 * Se voglio operare su tutti gli allegati della pratica: 2. contollo quelli sui quali voglio operare
			 */

			// 1.
			if (allegatiEsclusi.size() > 0) {
				for (Allegato allegato : allegatiEsclusi) {
					if (getApprovazioneFirmaTaskAttivoByAllegato(pratica, allegato) != null)
						return false;
				}
			}

			// 2. Se l'operazioe che voglio effettuare è conclusiva, ovvero conclude il processo di task di firma allora la riassegnazione è abilitata
			// Se non lo è allora controllo se è abilitato a riassegnare
			if (isOperazioneConclusiva)
				return true;

			for (Allegato allegato : allegati) {
				if (!riassegnazioneAbilitata(pratica, allegato))
					return false;
			}
		}

		return true;
	}

	private static boolean riassegnazioneAbilitata(Pratica<?> pratica, Allegato allegato) {

		XMLApprovazioneFirmaTask task = getApprovazioneFirmaTaskAttivoByAllegato(pratica, allegato);

		if (task != null) {
			int inApprovazione = 0;
			for (DestinatarioRichiestaApprovazioneFirmaTask destinatario : task.getDati().getDestinatari()) {

				if (StatoDestinatarioRichiestaApprovazioneFirmaTask.IN_APPROVAZIONE.equals(destinatario.getStato()))
					inApprovazione++;

				if (inApprovazione > 1)
					return false;
			}
		}

		return true;
	}

	public static boolean hasApprovazioneFirmaTask(Pratica<?> pratica) {

		for (Task<?> t : pratica.getTasks())
			if (t instanceof XMLApprovazioneFirmaTask && t.isAttivo() && !Boolean.FALSE.equals(((DatiApprovazioneFirmaTask) t.getDati()).getValido()))
				return true;

		return false;
	}

	public static List<XMLApprovazioneFirmaTask> getApprovazioneFirmaTaskAttivi(Pratica<?> pratica) {
		List<XMLApprovazioneFirmaTask> lista = new ArrayList<XMLApprovazioneFirmaTask>();
		for (Task<?> t : pratica.getTasks()) {
			if (t instanceof XMLApprovazioneFirmaTask && t.isAttivo() && !Boolean.FALSE.equals(((DatiApprovazioneFirmaTask) t.getDati()).getValido())) {
				XMLApprovazioneFirmaTask task = (XMLApprovazioneFirmaTask) t;
				lista.add(task);
			}
		}
		return lista;
	}

	public static XMLApprovazioneFirmaTask getApprovazioneFirmaTaskAttivoByAllegato(Pratica<?> pratica, Allegato a) {
		List<XMLApprovazioneFirmaTask> tasks = getApprovazioneFirmaTasksByAllegato(pratica, a.getNome(), true, true);

		if (!tasks.isEmpty() && tasks.size() != 1)
			throw new PraticaException("Trovati più task di firma attivi per l'allegato " + a.getNome());

		if (!tasks.isEmpty())
			return tasks.get(0);

		return null;
	}

	public static XMLApprovazioneFirmaTask getApprovazioneFirmaTaskAttivoByAllegato(Pratica<?> pratica, String nomeAllegato) {
		List<XMLApprovazioneFirmaTask> tasks = getApprovazioneFirmaTasksByAllegato(pratica, nomeAllegato, true, true);

		if (!tasks.isEmpty() && tasks.size() != 1)
			throw new PraticaException("Trovati più task di firma attivi per l'allegato " + nomeAllegato);

		if (!tasks.isEmpty())
			return tasks.get(0);

		return null;
	}

	public static List<XMLApprovazioneFirmaTask> getApprovazioneFirmaTasksByAllegato(Pratica<?> pratica, Allegato a, boolean checkAttivo, boolean checkValido) {
		return getApprovazioneFirmaTasksByAllegato(pratica, a.getNome(), checkAttivo, checkValido);
	}

	public static List<XMLApprovazioneFirmaTask> getApprovazioneFirmaTasksByAllegato(Pratica<?> pratica, String nomeAllegato, boolean checkAttivo, boolean checkValido) {
		List<XMLApprovazioneFirmaTask> lista = new ArrayList<XMLApprovazioneFirmaTask>();

		for (Task<?> t : pratica.getTasks()) {
			if (t instanceof XMLApprovazioneFirmaTask && (checkAttivo ? t.isAttivo() : true) && (checkValido ? !Boolean.FALSE.equals(((DatiApprovazioneFirmaTask) t.getDati()).getValido()) : true)) {
				XMLApprovazioneFirmaTask task = (XMLApprovazioneFirmaTask) t;
				RiferimentoAllegatoApprovazioneFirmaTask rif = task.getDati().getRiferimentoAllegato();
				if (nomeAllegato.equalsIgnoreCase(rif.getNome()))
					lista.add(task);
			}
		}

		return lista;
	}

	public static XMLApprovazioneFirmaTask getApprovazioneFirmaTaskById(Pratica<?> pratica, Integer id) {

		for (Task<?> t : pratica.getTasks()) {
			if (t instanceof XMLApprovazioneFirmaTask && t.getDati().getId().equals(id)) {
				return (XMLApprovazioneFirmaTask) t;
			}
		}
		throw new PraticaException("Task di richiesta firma non trovato.");
	}

	public static void storicizzaAllegato(Allegato a, Pratica<?> pratica, String versione, InformazioniTaskFirma infoTask, String utente) {

		Allegato allegato = null;

		if (XmlPluginUtil.isAllegatoProtocollato(a, pratica)) {
			allegato = XmlPluginUtil.getAllegatoProtocollato(a, pratica);

		} else {
			for (Allegato all : pratica.getDati().getAllegati()) {

				if (all.getNome().equalsIgnoreCase(a.getNome())) {
					allegato = all;
					break;
				}
			}
		}

		boolean found = false;

		if (allegato != null && infoTask != null) {
			for (StoricoVersioni storico : allegato.getStoricoVersioni()) {

				if (storico.getVersione().equalsIgnoreCase(versione)) {

					if (storico.getInformazioniTaskFirma() != null) {
						throw new PraticaException("Esistono già delle informazioni di proposta firma/visto " + "per la versione " + versione + " dell'allegato " + allegato.getNome());

					} else {
						found = true;
						storico.setInformazioniTaskFirma(infoTask);
					}

				}
			}
		}

		if (!found) {
			StoricoVersioni storicoVersioni = new StoricoVersioni(versione);
			storicoVersioni.setInformazioniTaskFirma(infoTask);
			storicoVersioni.setUtente(utente);
			allegato.getStoricoVersioni().add(storicoVersioni);
		}
	}

	public static void versionaRiferimentoAllegato(Allegato a, ApprovazioneFirmaTask task, InformazioniTaskFirma informazioniTaskFirma, String utente) {
		storicizzaAllegato(a, task.getEnclosingPratica(), a.getCurrentVersion(), informazioniTaskFirma, utente);
		task.getDati().getRiferimentoAllegato().setCurrentVersion(a.getCurrentVersion());
	}

	public static void cleanAllegatiProtocollati(Allegato a, Pratica<?> pratica) {
		if (XmlPluginUtil.isAllegatoProtocollato(a, pratica) && XmlPluginUtil.allegatoInPratica(a, pratica).size() != 0)
			pratica.getDati().getAllegati().removeAll(XmlPluginUtil.allegatoInPratica(a, pratica));
	}

	public static void unlockAllegato(Allegato a) {
		a.setLock(false);
		a.setLockedBy(null);
	}

	public static Allegato getAllegato(ApprovazioneFirmaTask task) {

		RiferimentoAllegatoApprovazioneFirmaTask rifAllegato = task.getDati().getRiferimentoAllegato();
		for (Allegato a : task.getEnclosingPratica().getDati().getAllegati()) {
			if (a.getNome().equalsIgnoreCase(rifAllegato.getNome()))
				return a;
		}
		throw new PraticaException("La pratica non contiene l'allegato del task di firma: " + rifAllegato.getNome());
	}

	public static void updateNotePratica(Pratica<?> pratica, String note) {
		if (GenericsUtil.isNotNullOrEmpty(note)) {
			StringBuffer sb = new StringBuffer();

			if (GenericsUtil.isNotNullOrEmpty(pratica.getDati().getNote()))
				sb.append(pratica.getDati().getNote()).append("\n");

			sb.append(note);
			pratica.getDati().setNote(sb.toString());
		}
	}

	public static it.eng.consolepec.xmlplugin.jaxb.DestinatarioRichiestaFirmaJaxb convertDestinatarioToJaxb(DestinatarioRichiestaApprovazioneFirmaTask destinatario) {

		if (destinatario instanceof DestinatarioUtenteRichiestaApprovazioneFirmaTask) {
			DestinatarioUtenteRichiestaApprovazioneFirmaTask d = (DestinatarioUtenteRichiestaApprovazioneFirmaTask) destinatario;
			it.eng.consolepec.xmlplugin.jaxb.DestinatarioUtenteRichiestaFirmaJaxb result = new it.eng.consolepec.xmlplugin.jaxb.DestinatarioUtenteRichiestaFirmaJaxb();
			result.setNomeUtente(d.getNomeUtente());
			result.setStato(d.getStato().name());
			result.setNome(d.getNome());
			result.setCognome(d.getCognome());
			result.setMatricola(d.getMatricola());
			result.setSettore(d.getSettore());
			return result;

		} else if (destinatario instanceof DestinatarioGruppoRichiestaApprovazioneFirmaTask) {
			DestinatarioGruppoRichiestaApprovazioneFirmaTask d = (DestinatarioGruppoRichiestaApprovazioneFirmaTask) destinatario;
			it.eng.consolepec.xmlplugin.jaxb.DestinatarioGruppoRichiestaFirmaJaxb result = new it.eng.consolepec.xmlplugin.jaxb.DestinatarioGruppoRichiestaFirmaJaxb();
			result.setNomeGruppo(d.getNomeGruppo());
			result.setStato(d.getStato().name());
			return result;

		} else {
			throw new IllegalArgumentException("Tipo destinatario non valido");
		}
	}

	public static DestinatarioRichiestaApprovazioneFirmaTask convertDestinatarioFromJaxb(it.eng.consolepec.xmlplugin.jaxb.DestinatarioRichiestaFirmaJaxb destinatario) {

		if (destinatario instanceof it.eng.consolepec.xmlplugin.jaxb.DestinatarioUtenteRichiestaFirmaJaxb) {

			it.eng.consolepec.xmlplugin.jaxb.DestinatarioUtenteRichiestaFirmaJaxb d = (it.eng.consolepec.xmlplugin.jaxb.DestinatarioUtenteRichiestaFirmaJaxb) destinatario;
			DestinatarioUtenteRichiestaApprovazioneFirmaTask result = new DestinatarioUtenteRichiestaApprovazioneFirmaTask(d.getNomeUtente(), d.getNome(), d.getCognome(), d.getMatricola(),
					d.getSettore(), StatoDestinatarioRichiestaApprovazioneFirmaTask.fromValue(d.getStato()));
			return result;

		} else if (destinatario instanceof it.eng.consolepec.xmlplugin.jaxb.DestinatarioGruppoRichiestaFirmaJaxb) {
			it.eng.consolepec.xmlplugin.jaxb.DestinatarioGruppoRichiestaFirmaJaxb d = (it.eng.consolepec.xmlplugin.jaxb.DestinatarioGruppoRichiestaFirmaJaxb) destinatario;
			DestinatarioGruppoRichiestaApprovazioneFirmaTask result = new DestinatarioGruppoRichiestaApprovazioneFirmaTask(d.getNomeGruppo(),
					StatoDestinatarioRichiestaApprovazioneFirmaTask.fromValue(d.getStato()));
			return result;

		} else {
			throw new IllegalArgumentException("Tipo destinatario non valido");
		}
	}

	public static boolean invalidaTaskFirmaPrecedentiConclusi(Allegato allegato, Pratica<?> pratica) {
		List<XMLApprovazioneFirmaTask> tasks = TaskDiFirmaUtil.getApprovazioneFirmaTasksByAllegato(pratica, allegato, false, false);
		for (XMLApprovazioneFirmaTask t : tasks) {

			if (!t.controllaAbilitazione(TipoApiTask.INVALIDA_TASK_FIRMA))
				return false;

			t.invalida();
		}

		return true;
	}
}
