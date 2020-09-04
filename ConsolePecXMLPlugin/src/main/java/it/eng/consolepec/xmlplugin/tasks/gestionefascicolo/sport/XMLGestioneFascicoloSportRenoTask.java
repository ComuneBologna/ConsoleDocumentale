package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport;

import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl.ConcludiFascicoloTaskApiImpl;

public class XMLGestioneFascicoloSportRenoTask extends XMLTaskFascicolo<DatiGestioneFascicoloSportRenoTask> implements GestioneFascicoloSportRenoTask {

	@Override
	protected DatiGestioneFascicoloSportRenoTask getDatiTask(Assegnatario asscorrente) {
		DatiGestioneFascicoloSportRenoTask.Builder builder = new DatiGestioneFascicoloSportRenoTask.Builder();
		builder.setAssegnatario(asscorrente);
		return builder.construct();
	}
	
	@Override
	protected void initApiTask() {
		operazioni.put(TipoApiTask.CONCLUDI_FASCICOLO, new ConcludiFascicoloTaskApiImpl<DatiGestioneFascicoloSportRenoTask>(this));
	}
}
