package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Stato;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.XMLFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.CambiaStatoFascicoloTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;

public class CambiaStatoFascicoloTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements CambiaStatoFascicoloTaskApi {

	public CambiaStatoFascicoloTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	public void cambiaStato(Stato stato) throws PraticaException {
		if(stato == null)
			throw new PraticaException("Il nuovo stato non può essere nullo");
		// cambio lo stato del fascicolo
		((XMLFascicolo) task.getEnclosingPratica()).getDatiPraticaTaskAdapter().setStato(stato);
		// Operazione di tracciatura eventuale dell'iter
		generaEvento(EventiIterFascicolo.CAMBIA_STATO_FASCICOLO, task.getCurrentUser(), task.getEnclosingPratica().getDati().getIdDocumentale(), stato.getLabel());

	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return getDatiFascicolo().getStato().equals(Stato.IN_GESTIONE) || getDatiFascicolo().getStato().equals(Stato.IN_VISIONE)  || getDatiFascicolo().getStato().equals(Stato.DA_INOLTRARE_ESTERNO);
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.CAMBIA_STATO_FASCICOLO;
	}

}
