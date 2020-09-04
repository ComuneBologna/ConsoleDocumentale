package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.albopretorio;

import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl.MettiInAffissioneTaskApiImpl;

public class XMLGestioneFascicoloAlboPretorioTask extends XMLTaskFascicolo<DatiGestioneFascicoloAlboPretorioTask> implements GestioneFascicoloAlboPretorioTask {
	
	@Override
	protected void initApiTask(){
		super.initApiTask();
		
		operazioni.put(TipoApiTask.METTI_IN_AFFISSIONE, new MettiInAffissioneTaskApiImpl<DatiGestioneFascicoloAlboPretorioTask>(this));
	}
	
	@Override
	protected DatiGestioneFascicoloAlboPretorioTask getDatiTask(Assegnatario asscorrente) {
		DatiGestioneFascicoloAlboPretorioTask.Builder builder = new DatiGestioneFascicoloAlboPretorioTask.Builder();
		builder.setAssegnatario(asscorrente);
		return builder.construct();
	}
}
