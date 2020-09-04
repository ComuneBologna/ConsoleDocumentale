package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Interfaccia che rappresenta una singola operazione eseguibile su un task
 */
public interface ITaskApi {

	public final static Logger _log = LoggerFactory.getLogger(ITaskApi.class);

	/* indica se la pratica è in uno stato per cui l'operazione può essere eseguita */
	public boolean isOperazioneAbilitata();

}
