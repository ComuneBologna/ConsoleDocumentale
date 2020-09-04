package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.elettorale;

import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;

public class XMLGestioneFascicoloElettoraleElettoreTask extends XMLTaskFascicolo<DatiGestioneFascicoloElettoraleElettoreTask> implements GestioneFascicoloElettoraleElettoreTask {

	@Override
	protected DatiGestioneFascicoloElettoraleElettoreTask getDatiTask(Assegnatario asscorrente) {
		DatiGestioneFascicoloElettoraleElettoreTask.Builder builder = new DatiGestioneFascicoloElettoraleElettoreTask.Builder();
		builder.setAssegnatario(asscorrente);
		return builder.construct();
	}
}
