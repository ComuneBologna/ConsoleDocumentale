package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import java.util.Arrays;
import java.util.List;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.ITipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.ModificaTipologieAllegatoTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.util.TaskDiFirmaUtil;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

/**
 * @author GiacomoFM
 * @since 12/dic/2018
 */
public class ModificaTipologieAllegatoTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements ModificaTipologieAllegatoTaskApi {

	public ModificaTipologieAllegatoTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}

	@Override
	protected ITipoApiTask getTipoApiTask() {
		return TipoApiTask.MODIFICA_TIPOLOGIE_ALLEGATO;
	}

	@Override
	public void modificaTipologieAllegato(final Allegato allegato, final List<String> tipologie) throws PraticaException {
		if (XmlPluginUtil.isAllegatoProtocollato(allegato, task.getEnclosingPratica()) || Boolean.TRUE.equals(allegato.getLock())
				|| TaskDiFirmaUtil.getApprovazioneFirmaTaskAttivoByAllegato(task.getEnclosingPratica(), allegato) != null) {
			throw new PraticaException("L'allegato [" + allegato.getNome() + "] è in lock");
		}

		allegato.getTipologiaDocumento().clear();

		if (tipologie == null || tipologie.isEmpty()) {
			generaEvento(EventiIterFascicolo.MODIFICA_TIPOLOGIE_ALLEGATO, task.getCurrentUser(), allegato.getNome(), "nessuna tipologia");

		} else {
			allegato.getTipologiaDocumento().addAll(tipologie);
			generaEvento(EventiIterFascicolo.MODIFICA_TIPOLOGIE_ALLEGATO, task.getCurrentUser(), allegato.getNome(), Arrays.toString(tipologie.toArray()));
		}

	}

}
