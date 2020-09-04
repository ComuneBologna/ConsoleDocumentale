package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.DatiGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.XMLGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.RimuoviAllegatoTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

public class RimuoviAllegatoTaskPECApiImpl<T extends DatiGestionePECTask> extends AbstractTaskPECApiImpl<T> implements RimuoviAllegatoTaskPECApi {

	public RimuoviAllegatoTaskPECApiImpl(XMLGestionePECTask task) {
		super(task);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return task.getEnclosingPratica().getDati().getAllegati().size() != 0;
	}

	@Override
	public void rimuoviAllegato(Allegato allegato) {
		if (!XmlPluginUtil.checkPresenzaAllegato(allegato, task.getEnclosingPratica()))
			throw new PraticaException("L'allegato " + allegato.getNome() + " non è presente nella PEC " + task.getEnclosingPratica().getDati().getIdDocumentale());

		boolean removed = task.getEnclosingPratica().getDati().getAllegati().remove(allegato);

		if (!removed)
			throw new PraticaException("Errore durante la rimozione dell'allegato " + allegato.getNome());

		generaEvento(EventiIterPEC.ELIMINA_ALLEGATO, task.getCurrentUser(), allegato.getNome());
	}

	@Override
	protected TipoApiTaskPEC getTipoApiTask() {
		return TipoApiTaskPEC.RIMUOVI_ALLEGATO;
	}

}
