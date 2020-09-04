package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.ITipoApiTask;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailOut;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.CreaBozzaTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;

public class CreaBozzaTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements CreaBozzaTaskApi {

	public CreaBozzaTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	public void creaBozza(PraticaEmailOut bozza) {
		if (bozza == null || !Stato.BOZZA.equals(bozza.getDati().getStato())) {
			throw new PraticaException("Bozza non valida");
		}

		generaEvento(EventiIterFascicolo.CREA_BOZZA, task.getCurrentUser(), bozza.getDati().getIdDocumentale());

	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}

	@Override
	protected ITipoApiTask getTipoApiTask() {
		return TipoApiTask.CREA_BOZZA;
	}

}
