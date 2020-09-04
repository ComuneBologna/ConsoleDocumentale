package it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.operazioni.impl;

import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.DatiGestioneComunicazioneTask;
import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.XMLGestioneComunicazioneTask;
import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.operazioni.CreaComunicazionePerCopiaTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.operazioni.TipoApiTaskComunicazione;

public class CreaComunicazionePerCopiaTaskApiImpl<T extends DatiGestioneComunicazioneTask> extends AbstractComunicazioneTaskApiImpl<T> implements CreaComunicazionePerCopiaTaskApi {

	public CreaComunicazionePerCopiaTaskApiImpl(XMLGestioneComunicazioneTask task) {
		super(task);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}

	@Override
	protected TipoApiTaskComunicazione getTipoApiTask() {
		return TipoApiTaskComunicazione.CREA_COMUNICAZIONE_PER_COPIA;
	}

	

}
