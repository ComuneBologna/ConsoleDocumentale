package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import it.eng.consolepec.xmlplugin.exception.PraticaException;

import java.util.Date;

public interface AggiungiProtocollazioneBA01TaskApi extends ITaskApi {

	public void aggiungiCapofilaFromBA01(String numeroPG, Integer annoPG, String titiolo, String rubrica, String sezione, String oggetto, Date dataProtocollazione, String dataArrivo, String oraArrivo) throws PraticaException;

}
