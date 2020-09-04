package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport;

import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;

public class XMLGestioneFascicoloSportSantoStefanoTask extends XMLTaskFascicolo<DatiGestioneFascicoloSportSantoStefanoTask> implements GestioneFascicoloSportSantoStefanoTask {

	@Override
	protected DatiGestioneFascicoloSportSantoStefanoTask getDatiTask(Assegnatario asscorrente) {
		DatiGestioneFascicoloSportSantoStefanoTask.Builder builder = new DatiGestioneFascicoloSportSantoStefanoTask.Builder();
		builder.setAssegnatario(asscorrente);
		return builder.construct();
	}
}
