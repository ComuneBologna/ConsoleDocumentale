package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport;

import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl.ConcludiFascicoloTaskApiImpl;

public class XMLGestioneFascicoloSportPortoTask extends XMLTaskFascicolo<DatiGestioneFascicoloSportPortoTask> implements GestioneFascicoloSportPortoTask {

	@Override
	protected DatiGestioneFascicoloSportPortoTask getDatiTask(Assegnatario asscorrente) {
		DatiGestioneFascicoloSportPortoTask.Builder builder = new DatiGestioneFascicoloSportPortoTask.Builder();
		builder.setAssegnatario(asscorrente);
		return builder.construct();
	}
	
	@Override
	protected void initApiTask() {
		operazioni.put(TipoApiTask.CONCLUDI_FASCICOLO, new ConcludiFascicoloTaskApiImpl<DatiGestioneFascicoloSportPortoTask>(this));
	}
}
