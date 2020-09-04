package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport;

import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;

public class XMLGestioneFascicoloSportBorgoPanigaleRenoTask extends XMLTaskFascicolo<DatiGestioneFascicoloSportBorgoPanigaleRenoTask> implements GestioneFascicoloSportBorgoPanigaleRenoTask {

	@Override
	protected DatiGestioneFascicoloSportBorgoPanigaleRenoTask getDatiTask(Assegnatario asscorrente) {
		DatiGestioneFascicoloSportBorgoPanigaleRenoTask.Builder builder = new DatiGestioneFascicoloSportBorgoPanigaleRenoTask.Builder();
		builder.setAssegnatario(asscorrente);
		return builder.construct();
	}
	

}
