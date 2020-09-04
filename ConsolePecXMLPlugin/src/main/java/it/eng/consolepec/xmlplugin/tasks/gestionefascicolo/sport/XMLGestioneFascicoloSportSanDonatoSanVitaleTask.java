package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport;

import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;

public class XMLGestioneFascicoloSportSanDonatoSanVitaleTask extends XMLTaskFascicolo<DatiGestioneFascicoloSportSanDonatoSanVitaleTask> implements GestioneFascicoloSportSanDonatoSanVitaleTask {

	@Override
	protected DatiGestioneFascicoloSportSanDonatoSanVitaleTask getDatiTask(Assegnatario asscorrente) {
		DatiGestioneFascicoloSportSanDonatoSanVitaleTask.Builder builder = new DatiGestioneFascicoloSportSanDonatoSanVitaleTask.Builder();
		builder.setAssegnatario(asscorrente);
		return builder.construct();
	}
	

}
