package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.RimuoviAllegatoTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.util.TaskDiFirmaUtil;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

public class RimuoviAllegatoTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements RimuoviAllegatoTaskApi {

	public RimuoviAllegatoTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	public void rimuoviAllegato(Allegato allegato) {

		if (!XmlPluginUtil.checkPresenzaAllegato(allegato, task.getEnclosingPratica())) {
			throw new PraticaException("L'allegato " + allegato.getNome() + " non è presente nel fascicolo " + task.getEnclosingPratica().getDati().getIdDocumentale());
		}

		if (!checkRimozione(allegato)) {
			throw new PraticaException("L'allegato " + allegato.getNome() + " non può essere eliminato");
		}

		TaskDiFirmaUtil.invalidaTaskFirmaPrecedentiConclusi(allegato, task.getEnclosingPratica());

		boolean removed = task.getEnclosingPratica().getDati().getAllegati().remove(allegato);

		if (!removed) {
			throw new PraticaException("Errore durante la rimozione dell'allegato " + allegato.getNome());
		}

		generaEvento(EventiIterFascicolo.ELIMINA_ALLEGATO, task.getCurrentUser(), allegato.getNome());
	}

	private boolean checkRimozione(Allegato allegato) {
		return !XmlPluginUtil.isAllegatoProtocollato(allegato, task.getEnclosingPratica()) //
				&& (TaskDiFirmaUtil.getApprovazioneFirmaTaskAttivoByAllegato(task.getEnclosingPratica(), allegato) == null) //
				&& (allegato.getLock() == null || !Boolean.TRUE.equals(allegato.getLock())); //
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return getDatiFascicolo().getAllegati().size() != 0;
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.RIMUOVI_ALLEGATO;
	}
}
