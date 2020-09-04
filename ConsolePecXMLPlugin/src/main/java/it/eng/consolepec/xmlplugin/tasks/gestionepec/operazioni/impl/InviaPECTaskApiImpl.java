package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl;

import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmail;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.DatiGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.GestionePECOutTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.XMLGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.InviaPECTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;

public class InviaPECTaskApiImpl<T extends DatiGestionePECTask> extends AbstractTaskPECApiImpl<T> implements InviaPECTaskApi {

	public InviaPECTaskApiImpl(XMLGestionePECTask task) {
		super(task);
	}

	@Override
	public void inviaPec() {
		PraticaEmail bozza = task.getEnclosingPratica();
		bozza.getDati().setStato(Stato.DA_INVIARE);

		if (bozza.getInCaricoA() != null) {
			task.rilasciaInCarico(bozza.getInCaricoA());
		}

		generaEvento(EventiIterPEC.INVIA_PEC, task.getCurrentUser());
	}

	@Override
	protected TipoApiTaskPEC getTipoApiTask() {
		return TipoApiTaskPEC.INVIA_PEC;
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		GestionePECOutTask pecOutTask = (GestionePECOutTask) task;
		return Stato.BOZZA.equals(task.getEnclosingPratica().getDati().getStato()) && pecOutTask.isEmailValida();
	}

}
