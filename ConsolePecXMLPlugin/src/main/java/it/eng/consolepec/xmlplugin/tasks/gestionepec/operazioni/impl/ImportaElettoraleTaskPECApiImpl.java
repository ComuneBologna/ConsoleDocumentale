package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl;

import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.DatiGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.XMLGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.ImportaElettoraleTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;

public class ImportaElettoraleTaskPECApiImpl <T extends DatiGestionePECTask> extends AbstractTaskPECApiImpl<T> implements ImportaElettoraleTaskPECApi{

	public ImportaElettoraleTaskPECApiImpl(XMLGestionePECTask task) {
		super(task);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		it.eng.consolepec.xmlplugin.pratica.email.DatiEmail dati = task.getEnclosingPratica().getDati();
		return dati.getStato().equals(Stato.IN_GESTIONE) && !task.getEnclosingPratica().hasPraticheCollegate() && dati.getAllegati().size() > 0;
	}

	@Override
	protected TipoApiTaskPEC getTipoApiTask() {
		return TipoApiTaskPEC.IMPORTA_ELETTORALE;
	}

}
