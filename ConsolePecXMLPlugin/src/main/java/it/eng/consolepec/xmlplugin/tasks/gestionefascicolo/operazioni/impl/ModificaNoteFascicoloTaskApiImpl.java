package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.consolepec.xmlplugin.factory.ITipoApiTask;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Stato;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.operazioni.ModificaNoteTaskApi;

public class ModificaNoteFascicoloTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements ModificaNoteTaskApi {

	public ModificaNoteFascicoloTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	public void aggiungiNote(String note) {
		String noteFascicolo = task.getEnclosingPratica().getDati().getNote();
		StringBuilder noteBuilder = null;

		if (noteFascicolo == null || noteFascicolo.trim().isEmpty()) {
			noteBuilder = new StringBuilder();

		} else {
			noteBuilder = new StringBuilder(noteFascicolo).append("\n");
		}

		noteBuilder.append(note);
		task.getEnclosingPratica().getDati().setNote(noteBuilder.toString());

		generaEvento(EventiIterFascicolo.AGGIUNTA_NOTE, task.getCurrentUser());
	}

	@Override
	public void modificaNote(String note) {
		task.getEnclosingPratica().getDati().setNote(note);
		generaEvento(EventiIterFascicolo.MODIFICA_NOTE, task.getCurrentUser());
	}

	@Override
	protected ITipoApiTask getTipoApiTask() {
		return TipoApiTask.MODIFICA_NOTE;
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return !Stato.ARCHIVIATO.equals(getDatiFascicolo().getStato());
	}

}
