package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;

public interface ModificaDatoAggiuntivoTaskApi extends ITaskApi {
	
	public void modificaDatoAggiuntivo(DatoAggiuntivo valore);
	
}
