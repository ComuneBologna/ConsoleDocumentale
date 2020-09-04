package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl;

import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.DatiGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.XMLGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.ArchiviaTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;

public class ArchiviaTaskPECApiImpl<T extends DatiGestionePECTask> extends AbstractTaskPECApiImpl<T> implements ArchiviaTaskPECApi {

	public ArchiviaTaskPECApiImpl(XMLGestionePECTask task) {
		super(task);
	}
	
	@Override
	public void archivia() {
		task.termina();
		task.getEnclosingPratica().getDati().setStato(Stato.ARCHIVIATA);
		
		if(task.getEnclosingPratica().getInCaricoA() != null)
			task.rilasciaInCarico(task.getEnclosingPratica().getInCaricoA());
		
		generaEvento(EventiIterPEC.ARCHIVIA, task.getCurrentUser());
	}


	@Override
	protected boolean controllaAbilitazioneInterna() {
		return task.getEnclosingPratica().getDati().getStato().name().equals(Stato.IN_GESTIONE.name());
		
	}

	@Override
	protected TipoApiTaskPEC getTipoApiTask() {
		return TipoApiTaskPEC.ARCHIVIA;
	}

}
