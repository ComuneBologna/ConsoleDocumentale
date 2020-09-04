package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl;

import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.DatiGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.XMLGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.ScartaTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;

public class ScartaTaskPECApiImpl<T extends DatiGestionePECTask> extends AbstractTaskPECApiImpl<T> implements ScartaTaskPECApi {

	public ScartaTaskPECApiImpl(XMLGestionePECTask task) {
		super(task);
	}

	@Override
	public void scarta() {
		task.termina();	
		task.getEnclosingPratica().getDati().setStato(Stato.SCARTATA);
		generaEvento( EventiIterPEC.SCARTA, task.getCurrentUser());
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return task.getEnclosingPratica().getDati().getStato().name().equals(Stato.IN_GESTIONE.name());
	}

	@Override
	protected TipoApiTaskPEC getTipoApiTask() {
		return TipoApiTaskPEC.SCARTA;
	}

}
