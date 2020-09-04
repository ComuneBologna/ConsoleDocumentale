package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl;

import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.DatiGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.GestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.XMLGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.AgganciaAFascicoloTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;

public class AgganciaAFascicoloTaskPECApiImpl<T extends DatiGestionePECTask> extends AbstractTaskPECApiImpl<T> implements AgganciaAFascicoloTaskPECApi {

	public AgganciaAFascicoloTaskPECApiImpl(XMLGestionePECTask task) {
		super(task);
	}
	

	@Override
	protected boolean controllaAbilitazioneInterna() {
		//return true;	// adesso ho una lista di pratiche collegate quindi posso aggiungere n-mila pratiche ad una email
		for (Task<?> t : task.getEnclosingPratica().getTasks()) {
			if (t instanceof GestionePECTask && ((GestionePECTask) t).isAttivo()) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected TipoApiTaskPEC getTipoApiTask() {
		return TipoApiTaskPEC.AGGANCIA_A_FASCICOLO;
	}

}
