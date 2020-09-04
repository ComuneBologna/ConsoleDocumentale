package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import java.util.List;

import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.cobo.consolepec.commons.exception.InvalidArgumentException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.ITipoApiTask;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Stato;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TagliaAllegatiTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.util.TaskDiFirmaUtil;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

/**
 *
 * @author biagiot
 *
 * @param <T>
 */
public class TagliaAllegatiTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements TagliaAllegatiTaskApi {

	public TagliaAllegatiTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	public TagliaAllegatiOutput tagliaAllegati(List<Allegato> allegati, Pratica<?> praticaDestinataria) throws ApplicationException, InvalidArgumentException {

		for (Allegato allegato : allegati) {
			if (!XmlPluginUtil.checkPresenzaAllegato(allegato, task.getEnclosingPratica()))
				throw new InvalidArgumentException("L'allegato " + allegato.getNome() + " non è presente nel fascicolo " + getDatiFascicolo().getIdDocumentale(), true);

			if (!checkTaglia(allegato))
				throw new InvalidArgumentException("L'allegato " + allegato.getNome() + " è bloccato e non può essere spostato dal fascicolo " + getDatiFascicolo().getIdDocumentale(), true);

			TaskDiFirmaUtil.invalidaTaskFirmaPrecedentiConclusi(allegato, task.getEnclosingPratica());
		}

		if (!tagliaAllegatiSorgente(allegati, praticaDestinataria))
			throw new ApplicationException("Errore durante l'eliminazione degli allegati", true);

		return new TagliaAllegatiOutput(allegati);
	}

	private boolean tagliaAllegatiSorgente(List<Allegato> allegatiNonProtocollati, Pratica<?> praticaDestinataria) {
		for (Allegato allegato : allegatiNonProtocollati) {
			if (!getDatiFascicolo().getAllegati().remove(allegato))
				return false;

			generaEvento(EventiIterFascicolo.TAGLIA_ALLEGATO, task.getCurrentUser(), allegato.getNome(), praticaDestinataria.getDati().getIdDocumentale());
		}

		return true;
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return !getDatiFascicolo().getAllegati().isEmpty() && Stato.IN_GESTIONE.equals(getDatiFascicolo().getStato());
	}

	@Override
	protected ITipoApiTask getTipoApiTask() {
		return TipoApiTask.TAGLIA_ALLEGATI;
	}

	private boolean checkTaglia(Allegato allegato) {
		return (TaskDiFirmaUtil.getApprovazioneFirmaTaskAttivoByAllegato(task.getEnclosingPratica(), allegato) == null) //
				&& (allegato.getLock() == null || !Boolean.TRUE.equals(allegato.getLock())) //
				&& (!XmlPluginUtil.isAllegatoProtocollato(allegato, task.getEnclosingPratica())); //
	}
}
