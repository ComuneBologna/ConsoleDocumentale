package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport;

import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl.ConcludiFascicoloTaskApiImpl;

public class XMLGestioneFascicoloSportSaragozzaTask extends XMLTaskFascicolo<DatiGestioneFascicoloSportSaragozzaTask> implements GestioneFascicoloSportSaragozzaTask {

	@Override
	protected DatiGestioneFascicoloSportSaragozzaTask getDatiTask(Assegnatario asscorrente) {
		DatiGestioneFascicoloSportSaragozzaTask.Builder builder = new DatiGestioneFascicoloSportSaragozzaTask.Builder();
		builder.setAssegnatario(asscorrente);
		return builder.construct();
	}
	
	@Override
	protected void initApiTask() {
		operazioni.put(TipoApiTask.CONCLUDI_FASCICOLO, new ConcludiFascicoloTaskApiImpl<DatiGestioneFascicoloSportSaragozzaTask>(this));
	}
}
