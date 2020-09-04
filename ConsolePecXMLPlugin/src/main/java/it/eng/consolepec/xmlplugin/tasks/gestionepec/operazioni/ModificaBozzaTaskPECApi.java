package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni;

import java.util.List;

import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.ITaskApi;

public interface ModificaBozzaTaskPECApi extends ITaskApi {

	void modificaBozza(String mittente, List<String> destinatari, List<String> destinatariCC, String oggetto, String body, String firma);

}
