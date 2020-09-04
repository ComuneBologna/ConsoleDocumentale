package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.consolepec.xmlplugin.exception.PraticaException;

public interface CambiaTipoFascicoloTaskApi extends ITaskApi {
	
	public void cambiaTipo(TipologiaPratica nuovoTipo) throws PraticaException;

}
