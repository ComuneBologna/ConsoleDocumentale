package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl;

import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.DatiGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.XMLGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.CreaFascicoloTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;

public class CreaFascicoloTaskPECApiImpl<T extends DatiGestionePECTask> extends AbstractTaskPECApiImpl<T> implements CreaFascicoloTaskPECApi {

	public CreaFascicoloTaskPECApiImpl(XMLGestionePECTask task) {
		super(task);
	}
	

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return !task.getEnclosingPratica().hasPraticheCollegate() && task.getEnclosingPratica().getDati().getStato().name().equals(Stato.IN_GESTIONE.name());
		
	}

	@Override
	protected TipoApiTaskPEC getTipoApiTask() {
		return TipoApiTaskPEC.CREA_FASCICOLO;
	}

}
