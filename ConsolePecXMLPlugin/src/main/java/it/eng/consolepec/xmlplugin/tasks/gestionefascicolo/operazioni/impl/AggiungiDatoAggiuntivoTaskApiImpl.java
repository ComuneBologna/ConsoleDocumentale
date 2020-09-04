package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.AggiungiDatoAggiuntivoTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;

import java.util.List;

public class AggiungiDatoAggiuntivoTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements AggiungiDatoAggiuntivoTaskApi {

	public AggiungiDatoAggiuntivoTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.AGGIUNGI_DATO_AGGIUNTIVO;
	}

	@Override
	public void aggiungiDatoAggiuntivo(DatoAggiuntivo datoAggiuntivo) {
		getDatiFascicolo().getDatiAggiuntivi().add(datoAggiuntivo);
	}

	@Override
	public void aggiungiDatiAggiuntivi(List<DatoAggiuntivo> datiAggiuntivi) {
		for (DatoAggiuntivo d : datiAggiuntivi)
			aggiungiDatoAggiuntivo(d);
	}
}
