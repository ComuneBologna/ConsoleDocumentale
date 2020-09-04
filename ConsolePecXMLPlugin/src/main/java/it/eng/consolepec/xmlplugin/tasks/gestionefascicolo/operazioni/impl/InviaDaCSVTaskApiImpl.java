package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.ITipoApiTask;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Stato;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.InviaDaCSVTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;

public class InviaDaCSVTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements InviaDaCSVTaskApi {

	public InviaDaCSVTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return Stato.IN_GESTIONE.equals(getDatiFascicolo().getStato());
	}

	@Override
	protected ITipoApiTask getTipoApiTask() {
		return TipoApiTask.INVIA_MAIL_DA_CSV;
	}

	@Override
	public void inviaDaCSV(String nomeAllegato) {

		boolean found = false;

		for (Allegato allegato : getDatiFascicolo().getAllegati()) {
			if (allegato.getNome().equals(nomeAllegato)) {
				found = true;
				break;
			}
		}

		if (!found) {
			throw new PraticaException("Allegato " + nomeAllegato + " non presente nel fascicolo " + getDatiFascicolo().getIdDocumentale());
		}

		generaEvento(EventiIterFascicolo.INVIA_MAIL_DA_CSV, task.getCurrentUser(), nomeAllegato);
	}

}
