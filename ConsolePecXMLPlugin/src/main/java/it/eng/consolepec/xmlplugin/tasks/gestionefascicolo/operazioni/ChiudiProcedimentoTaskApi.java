package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import it.eng.cobo.consolepec.commons.procedimento.ChiusuraProcedimentoInput;

public interface ChiudiProcedimentoTaskApi extends ITaskApi {

	public void chiudiProcedimento(ChiusuraProcedimentoInput chiusuraProcedimentoInput, Integer durata);

}
