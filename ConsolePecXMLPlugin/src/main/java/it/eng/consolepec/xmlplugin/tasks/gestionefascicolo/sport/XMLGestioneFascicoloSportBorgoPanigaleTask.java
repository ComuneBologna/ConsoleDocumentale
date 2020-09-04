package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport;

import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl.ConcludiFascicoloTaskApiImpl;

public class XMLGestioneFascicoloSportBorgoPanigaleTask extends XMLTaskFascicolo<DatiGestioneFascicoloSportBorgoPanigaleTask> implements GestioneFascicoloSportBorgoPanigaleTask {

	@Override
	protected DatiGestioneFascicoloSportBorgoPanigaleTask getDatiTask(Assegnatario asscorrente) {
		DatiGestioneFascicoloSportBorgoPanigaleTask.Builder builder = new DatiGestioneFascicoloSportBorgoPanigaleTask.Builder();
		builder.setAssegnatario(asscorrente);
		return builder.construct();
	}
	
	@Override
	protected void initApiTask() {
		operazioni.put(TipoApiTask.CONCLUDI_FASCICOLO, new ConcludiFascicoloTaskApiImpl<DatiGestioneFascicoloSportBorgoPanigaleTask>(this));
	}
}
