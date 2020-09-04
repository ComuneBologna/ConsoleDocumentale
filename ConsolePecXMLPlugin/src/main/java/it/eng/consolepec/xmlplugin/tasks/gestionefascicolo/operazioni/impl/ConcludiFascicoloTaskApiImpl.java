package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Stato;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.XMLFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.ConcludiFascicoloTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.util.TaskDiFirmaUtil;

public class ConcludiFascicoloTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements ConcludiFascicoloTaskApi {

	public ConcludiFascicoloTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void concludiFascicolo() throws PraticaException {
		if (controllaAbilitazioneInterna()) {
			((XMLFascicolo) task.getEnclosingPratica()).getDatiPraticaTaskAdapter().setStato(Stato.ARCHIVIATO);
			new TerminaApiTaskImpl(task).termina();
		} else
			throw new PraticaException("Operazione non consentita");

		generaEvento(EventiIterFascicolo.CONCLUDI, task.getCurrentUser());
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return task.isAttivo() 
				&& (getDatiFascicolo().getStato().equals(Stato.IN_GESTIONE) || getDatiFascicolo().getStato().equals(Stato.IN_VISIONE) || getDatiFascicolo().getStato().equals(Stato.IN_AFFISSIONE) || getDatiFascicolo().getStato().equals(Stato.DA_INOLTRARE_ESTERNO))
				&& !TaskDiFirmaUtil.hasApprovazioneFirmaTask(task.getEnclosingPratica());
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.CONCLUDI_FASCICOLO;
	}

}
