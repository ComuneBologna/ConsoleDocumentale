package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport;

import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;

public class XMLGestioneFascicoloSaluteSportTask extends XMLTaskFascicolo<DatiGestioneFascicoloSaluteSportTask> implements GestioneFascicoloSaluteSportTask {

	@Override
	protected DatiGestioneFascicoloSaluteSportTask getDatiTask(Assegnatario asscorrente) {
		DatiGestioneFascicoloSaluteSportTask.Builder builder = new DatiGestioneFascicoloSaluteSportTask.Builder();
		builder.setAssegnatario(asscorrente);
		return builder.construct();
	}
}
