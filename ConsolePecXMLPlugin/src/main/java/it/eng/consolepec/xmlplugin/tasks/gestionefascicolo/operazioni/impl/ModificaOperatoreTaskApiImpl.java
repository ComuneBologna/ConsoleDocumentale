package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.ModificaOperatoreTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;

public class ModificaOperatoreTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements ModificaOperatoreTaskApi {

	public ModificaOperatoreTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	
	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.MODIFICA_OPERATORE;
	}


	@Override
	public void modificaOperatore(String operatore) {
		getDatiFascicolo().getOperatore().setNome(operatore);
		generaEvento(EventiIterFascicolo.MODIFICA_OPERATORE, task.getCurrentUser(), operatore);
	}

}
