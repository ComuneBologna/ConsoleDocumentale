package it.eng.consolepec.xmlplugin.tasks.operazioni;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo.ConfigurazioneEsecuzione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.CondizioneEsecuzione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.ConseguenzaEsecuzione;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Notifica;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.ParametriExtra;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.TipologiaNotifica;
import it.eng.consolepec.xmlplugin.factory.ITipoApiTask;
import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.tasks.esecuzioni.Esecuzione;
import it.eng.consolepec.xmlplugin.tasks.eventiiter.EventiIter;
import it.eng.consolepec.xmlplugin.tasks.eventiiter.SerializzatoreEventiIter;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.ITaskApi;

public abstract class AbstractTaskApi implements ITaskApi {

	private Task<?> task;

	public AbstractTaskApi(Task<?> task) {
		this.task = task;
	}

	protected abstract ITipoApiTask getTipoApiTask();

	@Override
	public boolean isOperazioneAbilitata() {
		if (controllaAbilitazioneUtente() && controllaAbilitazioneOperazione()) {
			if (task.getCurrentUser() == null) {
				throw new IllegalStateException("current user is null");
			}

			return controllaAbilitazioneGenerica() && controllaAbilitazioneInterna() && task.isAttivo();
		}
		return false;
	}

	private boolean controllaAbilitazioneOperazione() {
		if (task.getOperazioniAbilitate().isEmpty()) {
			return true; // default: tutte le operazioni abilitate se non vengono assegnate restrizioni
		}
		return task.getOperazioniAbilitate().contains(getTipoApiTask().name());
	}

	/*
	 * controlli su utente (ES: utente esterno)
	 */
	protected boolean controllaAbilitazioneUtente() {
		return true;
	}

	protected boolean controllaAbilitazioneGenerica() {
		return true;
	}

	protected abstract boolean controllaAbilitazioneInterna();

	protected void generaEvento(EventiIter eventiIter, Object... params) {
		generaEvento(new ParametriExtra(), eventiIter, params);

	}

	protected void generaEvento(ParametriExtra parametriExtraDiNotifica, EventiIter eventiIter, Object... params) {
		new SerializzatoreEventiIter(task, eventiIter, params) //
				.serializeIter();
		aggiungNotifica(parametriExtraDiNotifica);
	}

	protected void generaEventoPerProtocollazione(EventiIter eventiIter, String numeroPG, Integer annoPG, Object... params) {
		new SerializzatoreEventiIter(task, eventiIter, params) //
				.serializeIter(numeroPG, annoPG);
	}

	protected void eseguiEsecuzione(final CondizioneEsecuzione condizione) {
		if (task.getEnclosingPratica() instanceof Fascicolo) {
			DatiFascicolo df = (DatiFascicolo) task.getEnclosingPratica().getDati();
			AnagraficaFascicolo anagraficaFascicolo = df.getAnagraficaFascicolo();

			for (ConfigurazioneEsecuzione configurazionEsecuzione : anagraficaFascicolo.getConfigurazioneEsecuzioni()) {

				if (configurazionEsecuzione.getCondizione().equals(condizione)) {

					for (ConseguenzaEsecuzione conseguenzaEsecuzione : configurazionEsecuzione.getEsecuzioni()) {
						Esecuzione<Task<?>, ConseguenzaEsecuzione> esecuzione = task.getContestoEsecuzione().calcola(conseguenzaEsecuzione.getClass());
						if (esecuzione != null) {
							esecuzione.esegui(task, conseguenzaEsecuzione);
						}
					}
				}
			}
		}

	}

	private void aggiungNotifica(ParametriExtra parametriDiNotifica) {
		String tipoApiCorrente = getTipoApiTask().toString();
		for (TipologiaNotifica t : TipologiaNotifica.values()) {
			if (TipologiaNotifica.EMAIL.equals(t) && parametriDiNotifica != null && parametriDiNotifica.getIndirizziEmail().isEmpty()) {
				continue;
			}
			task.getEnclosingPratica().getDati().getNotifiche().add(new Notifica(tipoApiCorrente, t, parametriDiNotifica));
		}
	}
}
