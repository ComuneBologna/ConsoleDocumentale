package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.ITipoApiTask;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Stato;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.XMLFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.RiattivaFascicoloTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;

public class RiattivaFascicoloTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements RiattivaFascicoloTaskApi {

	public RiattivaFascicoloTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	public void riattiva() {

		if (!controllaAbilitazioneInterna())
			throw new PraticaException("Il fascicolo non può essere riattivato");

		((XMLFascicolo) task.getEnclosingPratica()).getDatiPraticaTaskAdapter().setStato(Stato.IN_GESTIONE);
		task.getDati().setAttivo(true);
		task.getDati().getAssegnatario().setDataFine(null);
		generaEvento(EventiIterFascicolo.RIPORTA_IN_GESTIONE, task.getCurrentUser());
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return task.getEnclosingPratica().isRiattivabile();
	}

	@Override
	protected ITipoApiTask getTipoApiTask() {
		return TipoApiTask.RIPORTA_IN_GESTIONE;
	}

}
