package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.DatiGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.XMLGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.ModificaOperatoreTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;

public class ModificaOperatoreTaskPECApiImpl<T extends DatiGestionePECTask> extends AbstractTaskPECApiImpl<T> implements ModificaOperatoreTaskPECApi {

	public ModificaOperatoreTaskPECApiImpl(XMLGestionePECTask task) {
		super(task);
	}
	
	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}

	@Override
	protected TipoApiTaskPEC getTipoApiTask() {
		return TipoApiTaskPEC.MODIFICA_OPERATORE;
	}

	@Override
	public void modificaOperatore(String operatore) throws PraticaException {
		task.getEnclosingPratica().getDati().getOperatore().setNome(operatore);
		generaEvento(EventiIterPEC.MODIFICA_OPERATORE, task.getCurrentUser(), operatore);
	}

	

	

}
