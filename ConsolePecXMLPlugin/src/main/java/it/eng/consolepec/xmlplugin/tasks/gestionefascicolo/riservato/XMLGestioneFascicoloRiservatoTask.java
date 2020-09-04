package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.riservato;

import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;

public class XMLGestioneFascicoloRiservatoTask extends XMLTaskFascicolo<DatiGestioneFascicoloRiservatoTask> implements GestioneFascicoloRiservatoTask {

	@Override
	protected void initApiTask() {
		super.initApiTask();
		
		operazioni.remove(TipoApiTask.CAMBIA_VISIBILITA_FASCICOLO);
		operazioni.remove(TipoApiTask.PUBBLICA);
		operazioni.remove(TipoApiTask.RIMUOVI_PUBBLICAZIONE);
	}

	@Override
	protected DatiGestioneFascicoloRiservatoTask getDatiTask(Assegnatario asscorrente) {
		DatiGestioneFascicoloRiservatoTask.Builder builder = new DatiGestioneFascicoloRiservatoTask.Builder();
		builder.setAssegnatario(asscorrente);
		return builder.construct();
	}
}
