package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;

import java.util.List;

public interface AggiungiDatoAggiuntivoTaskApi extends ITaskApi {

	public void aggiungiDatoAggiuntivo(DatoAggiuntivo datoAggiuntivo);
	
	public void aggiungiDatiAggiuntivi(List<DatoAggiuntivo> datiAggiuntivi);
}
