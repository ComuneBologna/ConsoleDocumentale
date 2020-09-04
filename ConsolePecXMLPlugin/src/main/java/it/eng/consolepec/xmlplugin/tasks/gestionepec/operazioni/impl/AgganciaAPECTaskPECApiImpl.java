package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl;

import it.eng.consolepec.xmlplugin.tasks.gestionepec.DatiGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.XMLGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.AgganciaAPECTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;

public class AgganciaAPECTaskPECApiImpl<T extends DatiGestionePECTask> extends AbstractTaskPECApiImpl<T> implements AgganciaAPECTaskPECApi {

	public AgganciaAPECTaskPECApiImpl(XMLGestionePECTask task) {
		super(task);
	}
	

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
		
	}

	@Override
	protected TipoApiTaskPEC getTipoApiTask() {
		return TipoApiTaskPEC.AGGANCIA_A_PEC;
	}

}
