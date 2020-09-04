package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.util.GestioneProcedimentoBean;

public class AvviaProcedimentoSportTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AvviaProcedimentoTaskApiImpl<T> {

	public AvviaProcedimentoSportTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}
	
	@Override
	public void avviaProcedimento(GestioneProcedimentoBean avviaProcedimentoBean) {
		super.avviaProcedimento(avviaProcedimentoBean);
		DatiFascicolo datiFascicolo = getDatiFascicolo();
		if(datiFascicolo.getInCaricoA() != null)
			task.rilasciaInCarico(datiFascicolo.getInCaricoA());
	}

}
