package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import it.eng.consolepec.xmlplugin.util.GestioneProcedimentoBean;

public interface AvviaProcedimentoTaskApi extends ITaskApi {
		
	public void avviaProcedimento(GestioneProcedimentoBean avviaProcedimentoBean);

}
