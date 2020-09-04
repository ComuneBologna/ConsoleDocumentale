package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.consolepec.xmlplugin.exception.PraticaException;

public interface EliminaCondivisioneTaskApi extends ITaskApi {

	public void eliminaCondivisione(AnagraficaRuolo gruppo) throws PraticaException;

}
