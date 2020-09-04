package it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.operazioni.impl;

import it.eng.consolepec.xmlplugin.pratica.comunicazione.DatiComunicazione.Stato;
import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.DatiGestioneComunicazioneTask;
import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.XMLGestioneComunicazioneTask;
import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.operazioni.InviaComunicazioneTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.operazioni.TipoApiTaskComunicazione;

public class InviaComunicazioneTaskApiImpl<T extends DatiGestioneComunicazioneTask> extends AbstractComunicazioneTaskApiImpl<T> implements InviaComunicazioneTaskApi {

	public InviaComunicazioneTaskApiImpl(XMLGestioneComunicazioneTask task) {
		super(task);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return Stato.IN_GESTIONE.equals(getDatiComunicazione().getStato());
	}

	@Override
	protected TipoApiTaskComunicazione getTipoApiTask() {
		return TipoApiTaskComunicazione.INVIA_COMUNICAZIONE;
	}

	@Override
	public void inviata() {
		getDatiComunicazione().setStato(Stato.TERMINATA);
		generaEvento(EventiIterComunicazione.INVIATA, task.getCurrentUser());
		
	}

	

	@Override
	public void inviataTest() {
		generaEvento(EventiIterComunicazione.INVIATA_TEST, task.getCurrentUser());
		
	}

}
