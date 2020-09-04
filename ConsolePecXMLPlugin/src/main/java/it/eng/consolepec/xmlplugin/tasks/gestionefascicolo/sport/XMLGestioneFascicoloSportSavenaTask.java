package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport;

import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;

public class XMLGestioneFascicoloSportSavenaTask extends XMLTaskFascicolo<DatiGestioneFascicoloSportSavenaTask> implements GestioneFascicoloSportSavenaTask {

	@Override
	protected DatiGestioneFascicoloSportSavenaTask getDatiTask(Assegnatario asscorrente) {
		DatiGestioneFascicoloSportSavenaTask.Builder builder = new DatiGestioneFascicoloSportSavenaTask.Builder();
		builder.setAssegnatario(asscorrente);
		return builder.construct();
	}
}