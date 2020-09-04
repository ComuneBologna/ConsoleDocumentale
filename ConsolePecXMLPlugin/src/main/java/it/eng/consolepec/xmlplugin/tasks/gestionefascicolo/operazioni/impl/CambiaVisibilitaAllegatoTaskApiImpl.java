package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import java.util.List;
import java.util.TreeSet;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.GruppoVisibilita;
import it.eng.consolepec.xmlplugin.factory.ITipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.CambiaVisibilitaAllegatoTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.util.TaskDiFirmaUtil;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

public class CambiaVisibilitaAllegatoTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements CambiaVisibilitaAllegatoTaskApi {

	public CambiaVisibilitaAllegatoTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	public void cambiaVisibilitaAllegato(Allegato allegato, List<String> gruppi) {
		Allegato all = null;
		for (Allegato a : task.getEnclosingPratica().getDati().getAllegati()) {
			if (a.equals(allegato)) {
				all = allegato;
				break;
			}
		}

		if (all == null)
			throw new PraticaException("l'allegato " + allegato.getNome() + " non esiste nella pratica: " + task.getEnclosingPratica().getDati().getIdDocumentale());

		if (Boolean.TRUE.equals(all.getLock()) || TaskDiFirmaUtil.getApprovazioneFirmaTaskAttivoByAllegato(task.getEnclosingPratica(), all) != null) {
			throw new PraticaException("l'allegato " + allegato.getNome() + " è bloccato dal task: " + allegato.getLockedBy());
		}

		if (XmlPluginUtil.isAllegatoProtocollato(allegato, this.task.getEnclosingPratica())) {
			throw new PraticaException("l'allegato " + allegato.getNome() + " è protocollato");
		}

		TreeSet<GruppoVisibilita> newList = new TreeSet<GruppoVisibilita>();
		for (String gruppo : gruppi) {
			newList.add(new GruppoVisibilita(gruppo));
		}

		all.getGruppiVisibilita().clear();
		all.getGruppiVisibilita().addAll(newList);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}

	@Override
	protected ITipoApiTask getTipoApiTask() {
		return TipoApiTask.CAMBIA_VISIBILITA_ALLEGATO;
	}

}
