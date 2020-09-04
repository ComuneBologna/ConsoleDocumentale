package it.eng.portlet.consolepec.spring.bean.gestionepratiche.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.Operazione;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.ModificaRuoloAbilitazione;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailIn;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailOut;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Collegamento;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.pratica.modulistica.PraticaModulistica;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.TaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.riattiva.RiattivaFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.riattiva.RiattivaPECInTask;
import it.eng.consolepec.xmlplugin.tasks.riattiva.RiattivaPECOutTask;
import it.eng.consolepec.xmlplugin.tasks.riattiva.RiattivaPraticaModulisticaTask;
import it.eng.consolepec.xmlplugin.tasks.riattiva.XMLRiattivaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.ApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.XMLApprovazioneFirmaTask;
import it.eng.portlet.consolepec.spring.bean.configurazioni.GestioneConfigurazioni;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.GestioneTaskPratiche;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;

public class GestioneTaskPraticheImpl implements GestioneTaskPratiche {

	@Autowired
	GestioneProfilazioneUtente gestioneProfilazioneUtente;

	@Autowired
	GestioneConfigurazioni gestioneConfigurazioni;

	/**
	 * Ritorna un task del tipo passato, purché attivo Nel caso di più task, viene ritornato il primo trovato
	 *
	 * @param pratica - la pratica contenente il task
	 * @param clazz - il tipo di task
	 * @param ruoli - l'elenco di possibili assegnatari
	 * @return
	 */
	@Override
	public <T extends Task<?>> T estraiTaskCorrente(Pratica<?> pratica, Class<T> clazz, List<AnagraficaRuolo> anagraficheRuoli) {

		T taskCorrenteAssegnatario = estraiTaskCorrenteAssegnatario(pratica, clazz, anagraficheRuoli);

		if (taskCorrenteAssegnatario == null && (Fascicolo.class.isAssignableFrom(pratica.getClass()) && (clazz == null || (TaskFascicolo.class.isAssignableFrom(clazz))))) {

			taskCorrenteAssegnatario = estraiTaskCorrenteCollegato(pratica, clazz, anagraficheRuoli);
		}

		if (taskCorrenteAssegnatario == null && gestioneProfilazioneUtente.getDatiUtente().isUtenteEsterno()) {
			taskCorrenteAssegnatario = estraiTaskCorrenteUtenteEsterno(pratica, clazz);
		}

		return taskCorrenteAssegnatario;
	}

	private <T extends Task<?>> T estraiTaskCorrenteAssegnatario(Pratica<?> pratica, Class<T> clazz, List<AnagraficaRuolo> anagraficheRuoli) {

		for (Task<?> t : pratica.getTasks()) {
			final String assegnatario = t.getDati().getAssegnatario().getNome();

			if (t.isAttivo() && (clazz == null || clazz.isAssignableFrom(t.getClass()))) {
				AnagraficaRuolo ar = gestioneConfigurazioni.getAnagraficaRuolo(assegnatario);
				boolean isAssegnatario = anagraficheRuoli == null || anagraficheRuoli.contains(ar);

				if (isAssegnatario || gestioneProfilazioneUtente.isSuperutenteAbilitato(assegnatario, ModificaRuoloAbilitazione.class))
					return updateUser(t);
			}
		}

		return null;
	}

	private <T extends Task<?>> T estraiTaskCorrenteCollegato(Pratica<?> pratica, Class<T> clazz, List<AnagraficaRuolo> anagraficheRuoli) {

		for (Task<?> t : pratica.getTasks()) {
			if (t.isAttivo() && (clazz == null || clazz.isAssignableFrom(t.getClass())) && (TaskFascicolo.class == null || TaskFascicolo.class.isAssignableFrom(t.getClass()))) {
				TreeSet<Collegamento> collegamenti = ((DatiFascicolo) pratica.getDati()).getCollegamenti();
				for (Collegamento collegamento : collegamenti) {
					AnagraficaRuolo ar = gestioneConfigurazioni.getAnagraficaRuolo(collegamento.getNomeGruppo());

					if (anagraficheRuoli == null || anagraficheRuoli.contains(ar))
						return updateUser(t);
				}
			}
		}
		return null;
	}

	private <T extends Task<?>> T estraiTaskCorrenteUtenteEsterno(Pratica<?> pratica, Class<T> clazz) {
		for (Task<?> t : pratica.getTasks()) {
			if (t.isAttivo() && (clazz == null || clazz.isAssignableFrom(t.getClass()))) {
				return updateUser(t);
			}
		}
		return null;
	}

	@Override
	public Task<?> estraiTaskCorrente(Pratica<?> pratica, List<AnagraficaRuolo> anagraficheRuoli) {
		return estraiTaskCorrente(pratica, null, anagraficheRuoli);
	}

	@Override
	public Task<?> estraiTaskCorrente(Pratica<?> pratica) {
		for (Task<?> t : pratica.getTasks()) {
			if (t.isAttivo()) {
				return updateUser(t);
			}
		}
		return null;
	}

	/**
	 *
	 * Inietta l'utente che sta modificado il fascicolo
	 *
	 */
	@SuppressWarnings("unchecked")
	private <T extends Task<?>> T updateUser(Task<?> t) {
		t.setCurrentUser(gestioneProfilazioneUtente.getDatiUtente().getRuoloPersonale().getEtichetta());
		t.setUtenteEsterno(gestioneProfilazioneUtente.getDatiUtente().isUtenteEsterno());
		t.setOperazioniAbilitate(getOperazioniAbilitateUtente(t.getEnclosingPratica().getDati().getTipo()));
		return (T) t;
	}

	@Override
	public RiattivaPECInTask getRiattivaPecInTask(PraticaEmailIn email) {
		Set<Task<?>> tasks = email.getTasks();
		for (Task<?> t : tasks) {
			if (t != null && t instanceof RiattivaPECInTask && ((RiattivaPECInTask) t).isAttivo()) {
				RiattivaPECInTask task = (RiattivaPECInTask) t;
				return task;
			}
		}

		return null;
	}

	@Override
	public RiattivaPECOutTask estraiTaskRiattivazione(PraticaEmailOut email) {
		Set<Task<?>> tasks = email.getTasks();
		for (Task<?> t : tasks) {
			if (t != null && t instanceof RiattivaPECOutTask && ((RiattivaPECOutTask) t).isAttivo()) {
				RiattivaPECOutTask task = (RiattivaPECOutTask) t;
				return task;
			}
		}

		return null;
	}

	@Override
	public RiattivaFascicoloTask getRiattivaFascicoloTask(Fascicolo fascicolo) {
		Set<Task<?>> tasks = fascicolo.getTasks();
		for (Task<?> t : tasks) {

			final String assegnatario = t.getDati().getAssegnatario().getNome();
			AnagraficaRuolo ar = gestioneConfigurazioni.getAnagraficaRuolo(assegnatario);
			boolean isAssegnatario = gestioneProfilazioneUtente.getDatiUtente().getAnagraficheRuoli().contains(ar);

			// Assegnatario
			if (t instanceof RiattivaFascicoloTask && t.isAttivo()) {

				if (isAssegnatario || gestioneProfilazioneUtente.isSuperutenteAbilitato(assegnatario, ModificaRuoloAbilitazione.class)) {

					RiattivaFascicoloTask task = (RiattivaFascicoloTask) t;
					((XMLRiattivaTask) task).setCurrentUser(gestioneProfilazioneUtente.getDatiUtente().getRuoloPersonale().getEtichetta());
					return task;
				}
			}
		}

		return null;
	}

	@Override
	public RiattivaPraticaModulisticaTask getRiattivaPraticaModulisticaTask(PraticaModulistica praticaModulistica) {
		Set<Task<?>> tasks = praticaModulistica.getTasks();
		for (Task<?> t : tasks) {
			if (t != null && t instanceof RiattivaPraticaModulisticaTask && ((RiattivaPraticaModulisticaTask) t).isAttivo()) {
				RiattivaPraticaModulisticaTask task = (RiattivaPraticaModulisticaTask) t;
				((XMLRiattivaTask) task).setCurrentUser(gestioneProfilazioneUtente.getDatiUtente().getRuoloPersonale().getEtichetta());
				return task;
			}
		}

		return null;
	}

	@Override
	public <T extends Task<?>> T estraiTaskCorrenteSoloAssegnatario(Pratica<?> pratica, Class<T> clazz) {

		for (Task<?> t : pratica.getTasks()) {

			String assegnatario = t.getDati().getAssegnatario().getNome();

			if (t.isAttivo() && (clazz == null || clazz.isAssignableFrom(t.getClass()))) {

				AnagraficaRuolo ar = gestioneConfigurazioni.getAnagraficaRuolo(assegnatario);

				if (gestioneProfilazioneUtente.getDatiUtente().getAnagraficheRuoli().contains(ar))
					return updateUser(t);
			}
		}

		return null;
	}

	@Override
	public Task<?> estraiTaskCorrenteSoloAssegnatario(Pratica<?> pratica) {
		return estraiTaskCorrenteSoloAssegnatario(pratica, null);
	}

	@Override
	public Set<ApprovazioneFirmaTask> getAllTaskFirma(Pratica<?> pratica) {

		Set<ApprovazioneFirmaTask> result = new HashSet<ApprovazioneFirmaTask>();

		for (Task<?> t : pratica.getTasks()) {
			if (t instanceof XMLApprovazioneFirmaTask && t.isAttivo()) {
				result.add((ApprovazioneFirmaTask) t);
			}
		}

		return result;
	}

	private Set<String> getOperazioniAbilitateUtente(TipologiaPratica tipologiaPratica) {
		return Sets.newHashSet(Lists.transform(gestioneProfilazioneUtente.getOperazioniAbilitate(tipologiaPratica), new Function<Operazione, String>() {

			@Override
			public String apply(Operazione input) {
				return input.getNome();
			}
		}));
	}
}
