package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.protocollo;

import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl.MettiInAffissioneTaskApiImpl;

public class XMLGestioneFascicoloGenericoTask extends XMLTaskFascicolo<DatiGestioneFascicoloGenericoTask> implements GestioneFascicoloGenericoTask {

	@Override
	protected DatiGestioneFascicoloGenericoTask getDatiTask(Assegnatario asscorrente) {
		DatiGestioneFascicoloGenericoTask.Builder builder = new DatiGestioneFascicoloGenericoTask.Builder();
		builder.setAssegnatario(asscorrente);
		return builder.construct();
	}

	@Override
	protected void initApiTask() {
		super.initApiTask();

		operazioni.put(TipoApiTask.METTI_IN_AFFISSIONE, new MettiInAffissioneTaskApiImpl<DatiGestioneFascicoloGenericoTask>(this));

	}
}
