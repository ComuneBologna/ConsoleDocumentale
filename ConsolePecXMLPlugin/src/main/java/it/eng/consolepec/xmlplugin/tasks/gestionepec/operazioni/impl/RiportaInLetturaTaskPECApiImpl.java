package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.DatiGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.XMLGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.RiportaInLetturaTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;

public class RiportaInLetturaTaskPECApiImpl<T extends DatiGestionePECTask> extends AbstractTaskPECApiImpl<T> implements RiportaInLetturaTaskPECApi {

	public RiportaInLetturaTaskPECApiImpl(XMLGestionePECTask task) {
		super(task);
	}
	
	@Override
	public void riportaInLettura() throws PraticaException {
		task.getEnclosingPratica().getDati().setLetto(false);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return task.getEnclosingPratica().getDati().isLetto() == true;
	}

	@Override
	protected TipoApiTaskPEC getTipoApiTask() {
		return TipoApiTaskPEC.RIPORTA_IN_LETTURA;
	}

	

	

}
