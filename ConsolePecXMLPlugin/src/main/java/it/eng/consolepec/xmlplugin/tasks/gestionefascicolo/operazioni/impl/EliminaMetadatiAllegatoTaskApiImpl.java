package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import java.util.Iterator;
import java.util.List;

import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.ITipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.EliminaMetadatiAllegatoTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.util.TaskDiFirmaUtil;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

/**
 * @author GiacomoFM
 * @since 14/mar/2018
 */
public class EliminaMetadatiAllegatoTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements EliminaMetadatiAllegatoTaskApi {

	public EliminaMetadatiAllegatoTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}

	@Override
	protected ITipoApiTask getTipoApiTask() {
		return TipoApiTask.ELIMINA_METADATI_ALLEGATO;
	}

	@Override
	public void eliminaMetadatiAllegato(final Allegato allegato, final List<String> nomiMetadati) {

		if (XmlPluginUtil.isAllegatoProtocollato(allegato, this.task.getEnclosingPratica()) || Boolean.TRUE.equals(allegato.getLock())
				|| TaskDiFirmaUtil.getApprovazioneFirmaTaskAttivoByAllegato(task.getEnclosingPratica(), allegato) != null) {
			throw new PraticaException("L'allegato è in lock");
		}

		for (Iterator<DatoAggiuntivo> i = allegato.getDatiAggiuntivi().iterator(); i.hasNext();) {
			DatoAggiuntivo d = i.next();
			if (nomiMetadati.contains(d.getNome())) {
				log.trace("Rimozione dato aggiuntivo " + d.getNome());
				i.remove();
			}
		}
	}

}
