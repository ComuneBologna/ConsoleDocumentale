package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.ITipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.EvadiApprovazioneFirmaTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.StatoRichiestaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.XMLApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.operazioni.AbstractApprovazioneFirmaTaskApi;

import java.util.List;

/**
 *
 * @author biagiot
 *
 */
public class EvadiApprovazioneFirmaTaskApiImpl extends AbstractApprovazioneFirmaTaskApi implements EvadiApprovazioneFirmaTaskApi {

	public EvadiApprovazioneFirmaTaskApiImpl(XMLApprovazioneFirmaTask task) {
		super(task);
	}

	@Override
	public void evadi(List<String> ruoli) {
		if (!ruoli.contains(task.getDati().getAssegnatario().getNome()))
			throw new PraticaException("L'utente non è abilitato per l'operazione Evadi");

		task.getDati().setStato(StatoRichiestaApprovazioneFirmaTask.EVASO.name());
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return !Boolean.TRUE.equals(task.getDati().getAttivo()) && !StatoRichiestaApprovazioneFirmaTask.IN_APPROVAZIONE.name().equalsIgnoreCase(task.getDati().getStato());
	}

	@Override
	protected ITipoApiTask getTipoApiTask() {
		return TipoApiTask.EVADI_TASK_FIRMA;
	}
}