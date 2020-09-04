package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fatturazione;

import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;

public class XMLGestioneFascicoloFatturazioneTask extends XMLTaskFascicolo<DatiGestioneFascicoloFatturazioneTask> implements GestioneFascicoloFatturazioneTask {

	@Override
	protected DatiGestioneFascicoloFatturazioneTask getDatiTask(Assegnatario asscorrente) {
		DatiGestioneFascicoloFatturazioneTask.Builder builder = new DatiGestioneFascicoloFatturazioneTask.Builder();
		builder.setAssegnatario(asscorrente);
		return builder.construct();
	}
}
