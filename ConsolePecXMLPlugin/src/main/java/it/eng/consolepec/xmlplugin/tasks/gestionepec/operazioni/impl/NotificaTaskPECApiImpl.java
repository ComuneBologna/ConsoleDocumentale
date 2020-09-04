package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl;

import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.DatiGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.XMLGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.NotificaTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;

public class NotificaTaskPECApiImpl<T extends DatiGestionePECTask> extends AbstractTaskPECApiImpl<T> implements NotificaTaskPECApi {

	public NotificaTaskPECApiImpl(XMLGestionePECTask task) {
		super(task);
	}
	
	@Override
	public void notifica() {
		task.termina();
		task.getEnclosingPratica().getDati().setStato(Stato.NOTIFICATA);
		
		generaEvento(EventiIterPEC.NOTIFICA, task.getCurrentUser());
	}


	@Override
	protected boolean controllaAbilitazioneInterna() {
		return task.getEnclosingPratica().getDati().getStato().name().equals(Stato.IN_GESTIONE.name());
		
	}

	@Override
	protected TipoApiTaskPEC getTipoApiTask() {
		return TipoApiTaskPEC.NOTIFICA;
	}

}
