package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.consolepec.xmlplugin.factory.ITipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.EmissionePermessoTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;

public class EmissionePermessoTaskApiImpl<T extends DatiGestioneFascicoloTask>  extends AbstractTaskApiImpl<T> implements EmissionePermessoTaskApi {

	public EmissionePermessoTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}
	
	@Override
	protected ITipoApiTask getTipoApiTask() {
		return TipoApiTask.EMISSIONE_PERMESSO;
	}
	

}