package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl;

import it.eng.consolepec.xmlplugin.tasks.gestionepec.DatiGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.XMLGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;
import it.eng.consolepec.xmlplugin.tasks.operazioni.ModificaNoteTaskApi;

public class ModificaNotePECInTaskApiImpl<T extends DatiGestionePECTask> extends AbstractTaskPECApiImpl<T> implements ModificaNoteTaskApi {

	public ModificaNotePECInTaskApiImpl(XMLGestionePECTask task) {
		super(task);
	}

	@Override
	public void aggiungiNote(String note) {
		String notePecIn = task.getEnclosingPratica().getDati().getNote();
		StringBuilder noteBuilder = null;

		if (notePecIn == null || notePecIn.trim().isEmpty()) {
			noteBuilder = new StringBuilder();

		} else {
			noteBuilder = new StringBuilder(notePecIn).append("\n");
		}

		noteBuilder.append(note);
		task.getEnclosingPratica().getDati().setNote(noteBuilder.toString());

		generaEvento(EventiIterPEC.AGGIUNTA_NOTE, task.getCurrentUser());
	}

	@Override
	public void modificaNote(String note) {
		task.getEnclosingPratica().getDati().setNote(note);
		generaEvento(EventiIterPEC.MODIFICA_NOTE, task.getCurrentUser());
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}

	@Override
	protected TipoApiTaskPEC getTipoApiTask() {
		return TipoApiTaskPEC.MODIFICA_NOTE;
	}

}
