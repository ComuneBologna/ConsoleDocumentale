package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import java.util.List;

import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.ITipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.InserisciModificaMetadatiAllegatoTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.util.TaskDiFirmaUtil;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

/**
 * @author GiacomoFM
 * @since 14/mar/2018
 */
public class InserisciModificaMetadatiAllegatoTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements InserisciModificaMetadatiAllegatoTaskApi {

	public InserisciModificaMetadatiAllegatoTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}

	@Override
	protected ITipoApiTask getTipoApiTask() {
		return TipoApiTask.INSERISCI_O_MODIFICA_METADATI_ALLEGATO;
	}

	@Override
	public void inserisciModificaMetadatiAllegato(final Allegato allegato, final List<DatoAggiuntivo> datiAggiuntivi) {

		if (XmlPluginUtil.isAllegatoProtocollato(allegato, this.task.getEnclosingPratica()) || Boolean.TRUE.equals(allegato.getLock())
				|| TaskDiFirmaUtil.getApprovazioneFirmaTaskAttivoByAllegato(task.getEnclosingPratica(), allegato) != null) {
			throw new PraticaException("L'allegato è in lock");
		}

		for (DatoAggiuntivo datoAggiuntivo : datiAggiuntivi) {
			DatoAggiuntivo da = controllaPresenza(allegato, datoAggiuntivo.getNome());
			if (da != null) {
				allegato.getDatiAggiuntivi().remove(da);
			}
			allegato.getDatiAggiuntivi().add(datoAggiuntivo);
		}
	}

	private static DatoAggiuntivo controllaPresenza(final Allegato allegato, final String nome) {
		for (DatoAggiuntivo da : allegato.getDatiAggiuntivi()) {
			if (da.getNome().equals(nome)) {
				return da;
			}
		}
		return null;
	}

}
