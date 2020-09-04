package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport;

import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl.ConcludiFascicoloTaskApiImpl;

public class XMLGestioneFascicoloSportSanVitaleTask extends XMLTaskFascicolo<DatiGestioneFascicoloSportSanVitaleTask> implements GestioneFascicoloSportSanVitaleTask {

	@Override
	protected DatiGestioneFascicoloSportSanVitaleTask getDatiTask(Assegnatario asscorrente) {
		DatiGestioneFascicoloSportSanVitaleTask.Builder builder = new DatiGestioneFascicoloSportSanVitaleTask.Builder();
		builder.setAssegnatario(asscorrente);
		return builder.construct();
	}
	
	@Override
	protected void initApiTask() {
		operazioni.put(TipoApiTask.CONCLUDI_FASCICOLO, new ConcludiFascicoloTaskApiImpl<DatiGestioneFascicoloSportSanVitaleTask>(this));
	}
}
