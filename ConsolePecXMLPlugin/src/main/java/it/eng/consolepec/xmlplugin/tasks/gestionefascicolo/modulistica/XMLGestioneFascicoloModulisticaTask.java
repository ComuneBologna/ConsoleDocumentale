package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.modulistica;

import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;

public class XMLGestioneFascicoloModulisticaTask extends XMLTaskFascicolo<DatiGestioneFascicoloModulisticaTask> implements GestioneFascicoloModulisticaTask {

	@Override
	protected DatiGestioneFascicoloModulisticaTask getDatiTask(Assegnatario asscorrente) {
		DatiGestioneFascicoloModulisticaTask.Builder builder = new DatiGestioneFascicoloModulisticaTask.Builder();
		builder.setAssegnatario(asscorrente);
		return builder.construct();
	}
}
