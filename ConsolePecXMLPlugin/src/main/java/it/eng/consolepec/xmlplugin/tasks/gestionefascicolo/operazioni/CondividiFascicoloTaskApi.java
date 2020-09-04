package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.consolepec.xmlplugin.exception.PraticaException;

import java.util.List;

public interface CondividiFascicoloTaskApi extends ITaskApi {

	public void condividi(AnagraficaRuolo gruppo, List<String> operazioni) throws PraticaException;

}
