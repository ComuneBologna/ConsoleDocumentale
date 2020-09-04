package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import java.util.Iterator;

import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.RimuoviDatoAggiuntivoTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;

public class RimuoviDatoAggiuntivoTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements RimuoviDatoAggiuntivoTaskApi {

	public RimuoviDatoAggiuntivoTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.RIMUOVI_DATO_AGGIUNTIVO;
	}

	@Override
	public void rimuoviDatoAggiuntivo(String nomeCampo) {
		internalRimuoviDatoAggiuntivo(nomeCampo, false);
	}

	@Override
	public void rimuoviDatoAggiuntivoIfExists(String nomeCampo) {
		internalRimuoviDatoAggiuntivo(nomeCampo, true);
	}

	private void internalRimuoviDatoAggiuntivo(String nomeCampo, boolean safe) {

		Iterator<DatoAggiuntivo> iterator = getDatiFascicolo().getDatiAggiuntivi().iterator();

		while (iterator.hasNext()) {
			DatoAggiuntivo datoAggiuntivo = iterator.next();
			if (datoAggiuntivo.getNome().equals(nomeCampo)) {
				iterator.remove();
				return;
			}
		}

		if (!safe) {
			throw new PraticaException("Dato Aggiuntivo non presente: " + nomeCampo);
		}
	}

	@Override
	public void rimuoviDatiAggiuntivi() {
		boolean rimosso = false;
		if (getDatiFascicolo().getDatiAggiuntivi() != null) {
			StringBuilder sb = new StringBuilder();
			Iterator<DatoAggiuntivo> iterator = getDatiFascicolo().getDatiAggiuntivi().iterator();
			while (iterator.hasNext()) {
				sb.append(", ").append(iterator.next().getDescrizione());
				iterator.remove();
				rimosso = true;
			}

			if (rimosso) {
				generaEvento(EventiIterFascicolo.RIMUOVI_DATI_AGGIUNTIVI, task.getCurrentUser(), sb.substring(2));
			}
		}
	}

}
