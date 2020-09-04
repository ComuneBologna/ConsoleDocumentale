package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport;

import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;

public class XMLGestioneFascicoloSportNavileTask extends XMLTaskFascicolo<DatiGestioneFascicoloSportNavileTask> implements GestioneFascicoloSportNavileTask {

	@Override
	protected DatiGestioneFascicoloSportNavileTask getDatiTask(Assegnatario asscorrente) {
		DatiGestioneFascicoloSportNavileTask.Builder builder = new DatiGestioneFascicoloSportNavileTask.Builder();
		builder.setAssegnatario(asscorrente);
		return builder.construct();
	}

}
