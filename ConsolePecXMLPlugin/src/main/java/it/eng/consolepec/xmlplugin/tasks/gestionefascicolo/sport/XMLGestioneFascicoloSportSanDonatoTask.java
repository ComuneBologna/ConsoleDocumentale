package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport;

import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl.ConcludiFascicoloTaskApiImpl;

public class XMLGestioneFascicoloSportSanDonatoTask extends XMLTaskFascicolo<DatiGestioneFascicoloSportSanDonatoTask> implements GestioneFascicoloSportSanDonatoTask {

	@Override
	protected DatiGestioneFascicoloSportSanDonatoTask getDatiTask(Assegnatario asscorrente) {
		DatiGestioneFascicoloSportSanDonatoTask.Builder builder = new DatiGestioneFascicoloSportSanDonatoTask.Builder();
		builder.setAssegnatario(asscorrente);
		return builder.construct();
	}
	
	@Override
	protected void initApiTask() {
		operazioni.put(TipoApiTask.CONCLUDI_FASCICOLO, new ConcludiFascicoloTaskApiImpl<DatiGestioneFascicoloSportSanDonatoTask>(this));
	}
}
