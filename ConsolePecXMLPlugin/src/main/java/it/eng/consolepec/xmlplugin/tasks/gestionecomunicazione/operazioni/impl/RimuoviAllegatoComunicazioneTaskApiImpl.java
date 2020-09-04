package it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.operazioni.impl;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.pratica.comunicazione.DatiComunicazione.Stato;
import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.DatiGestioneComunicazioneTask;
import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.XMLGestioneComunicazioneTask;
import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.operazioni.RimuoviAllegatoComunicazioneTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.operazioni.TipoApiTaskComunicazione;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

public class RimuoviAllegatoComunicazioneTaskApiImpl<T extends DatiGestioneComunicazioneTask> extends AbstractComunicazioneTaskApiImpl<T> implements RimuoviAllegatoComunicazioneTaskApi {

	public RimuoviAllegatoComunicazioneTaskApiImpl(XMLGestioneComunicazioneTask task) {
		super(task);
	}

	@Override
	public void rimuoviAllegato(Allegato allegato) {
		if (!XmlPluginUtil.checkPresenzaAllegato(allegato, task.getEnclosingPratica()))
			throw new PraticaException("L'allegato " + allegato.getNome() + " non è presente nella comunicazione " + task.getEnclosingPratica().getDati().getIdDocumentale());

		boolean removed = task.getEnclosingPratica().getDati().getAllegati().remove(allegato);

		if (!removed)
			throw new PraticaException("Errore durante la rimozione dell'allegato " + allegato.getNome());

		generaEvento(EventiIterComunicazione.ELIMINA_ALLEGATO, task.getCurrentUser(), allegato.getNome());
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return Stato.IN_GESTIONE.equals(getDatiComunicazione().getStato()) && getDatiComunicazione().getAllegati().size() != 0;
	}

	@Override
	protected TipoApiTaskComunicazione getTipoApiTask() {
		return TipoApiTaskComunicazione.RIMUOVI_ALLEGATO;
	}
}
