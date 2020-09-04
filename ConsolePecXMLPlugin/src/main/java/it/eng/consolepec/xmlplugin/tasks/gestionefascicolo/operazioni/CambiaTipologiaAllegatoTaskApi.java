package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import it.eng.consolepec.xmlplugin.exception.PraticaException;

public interface CambiaTipologiaAllegatoTaskApi extends ITaskApi {

	public void cambiaTipologiaAllegato(String nomeAllegato, String tipologiaAllegato) throws PraticaException;

	public void modificaTipologiaAllegato(String nomeAllegato, String tipologiaAllegato) throws PraticaException;

}
