package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import java.util.List;

import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.cobo.consolepec.commons.exception.InvalidArgumentException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.ITipoApiTask;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.factory.TaskObserver.IncollaAllegatoHandler;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Stato;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.IncollaAllegatiTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TagliaAllegatiTaskApi.TagliaAllegatiOutput;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.util.TaskDiFirmaUtil;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

/**
 * 
 * @author biagiot
 *
 * @param <T>
 */
public class IncollaAllegatiTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements IncollaAllegatiTaskApi {

	public IncollaAllegatiTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	public void incollaAllegati(TagliaAllegatiOutput tagliaAllegatiOutput, Pratica<?> praticaSorgente, IncollaAllegatoHandler handler) throws ApplicationException, InvalidArgumentException {
		incollaAllegati(tagliaAllegatiOutput.getAllegati(), handler, praticaSorgente);
	}

	private void incollaAllegati(List<Allegato> allegatiNonProtocollati, IncollaAllegatoHandler handler, Pratica<?> praticaSorgente) throws ApplicationException, InvalidArgumentException {

		for (Allegato daIncollare : allegatiNonProtocollati) {
			String nome = getNomeAllegato(daIncollare);
			Allegato incollato = handler.incollaAllegato(nome, daIncollare);
			TaskDiFirmaUtil.invalidaTaskFirmaPrecedentiConclusi(incollato, task.getEnclosingPratica());
			getDatiFascicolo().getAllegati().add(incollato);
			generaEvento(EventiIterFascicolo.INCOLLA_ALLEGATO, task.getCurrentUser(), incollato.getNome(), praticaSorgente.getDati().getIdDocumentale());
		}
	}

	private String getNomeAllegato(Allegato allegato) {

		if (TaskDiFirmaUtil.getApprovazioneFirmaTaskAttivoByAllegato(task.getEnclosingPratica(), allegato) != null || //
				XmlPluginUtil.isAllegatoProtocollato(allegato, task.getEnclosingPratica()) || //
				XmlPluginUtil.hasLock(allegato, task.getEnclosingPratica()) || //
				Boolean.FALSE.equals(allegato.getVersionable()))

			return XmlPluginUtil.getNewNomeAllegatoFascicolo(allegato, task.getEnclosingPratica());

		return allegato.getNome();
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return Stato.IN_GESTIONE.equals(getDatiFascicolo().getStato());
	}

	@Override
	protected ITipoApiTask getTipoApiTask() {
		return TipoApiTask.INCOLLA_ALLEGATI;
	}

}
