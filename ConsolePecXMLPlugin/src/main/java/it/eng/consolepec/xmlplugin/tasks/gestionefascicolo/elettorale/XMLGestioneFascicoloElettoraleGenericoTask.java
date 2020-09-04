package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.elettorale;

import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;

public class XMLGestioneFascicoloElettoraleGenericoTask extends XMLTaskFascicolo<DatiGestioneFascicoloElettoraleGenericoTask> implements GestioneFascicoloElettoraleGenericoTask {

	@Override
	protected DatiGestioneFascicoloElettoraleGenericoTask getDatiTask(Assegnatario asscorrente) {
		DatiGestioneFascicoloElettoraleGenericoTask.Builder builder = new DatiGestioneFascicoloElettoraleGenericoTask.Builder();
		builder.setAssegnatario(asscorrente);
		return builder.construct();
	}
}
