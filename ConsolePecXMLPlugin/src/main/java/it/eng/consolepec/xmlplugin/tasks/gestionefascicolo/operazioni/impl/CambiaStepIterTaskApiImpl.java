package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.consolepec.xmlplugin.factory.ITipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;

import java.util.List;

public class CambiaStepIterTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractCambiaStepIterTaskApiImpl<T> {

	public CambiaStepIterTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	protected ITipoApiTask getTipoApiTask() {
		return TipoApiTask.CAMBIA_STEP_ITER;
	}

	@Override
	public void cambiaStep(String step, boolean finale, boolean iniziale, boolean creaBozza) {
		super.cambiaStep(step, finale, iniziale, creaBozza);
	}
	
	@Override
	public void cambiaStep(String step, boolean finale, boolean iniziale, boolean creaBozza, List<String> destinatariNotifica) {
		throw new UnsupportedOperationException();
	}
}
