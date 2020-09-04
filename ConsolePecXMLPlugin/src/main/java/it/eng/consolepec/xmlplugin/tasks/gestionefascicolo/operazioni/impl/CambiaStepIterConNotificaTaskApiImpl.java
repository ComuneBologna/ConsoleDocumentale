package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.consolepec.xmlplugin.factory.ITipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;

import java.util.List;

public class CambiaStepIterConNotificaTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractCambiaStepIterTaskApiImpl<T> {

	public CambiaStepIterConNotificaTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	protected ITipoApiTask getTipoApiTask() {
		return TipoApiTask.CAMBIA_STEP_ITER_CON_NOTIFICA;
	}

	@Override
	public void cambiaStep(String step, boolean finale, boolean iniziale, boolean creaBozza) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void cambiaStep(String step, boolean finale, boolean iniziale, boolean creaBozza, List<String> destinatariNotifica) {
		super.cambiaStep(step, finale, iniziale, creaBozza, destinatariNotifica);
	}
	
}
