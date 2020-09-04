package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport;

import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;

public class XMLGestioneFascicoloSportPortoSaragozzaTask extends XMLTaskFascicolo<DatiGestioneFascicoloSportPortoSaragozzaTask> implements GestioneFascicoloSportPortoSaragozzaTask {

	@Override
	protected DatiGestioneFascicoloSportPortoSaragozzaTask getDatiTask(Assegnatario asscorrente) {
		DatiGestioneFascicoloSportPortoSaragozzaTask.Builder builder = new DatiGestioneFascicoloSportPortoSaragozzaTask.Builder();
		builder.setAssegnatario(asscorrente);
		return builder.construct();
	}

}
