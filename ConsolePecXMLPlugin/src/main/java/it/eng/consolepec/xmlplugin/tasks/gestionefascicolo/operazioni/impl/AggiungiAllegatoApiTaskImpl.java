package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import java.util.List;
import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.cobo.consolepec.commons.exception.InvalidArgumentException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.TaskObserver.AggiungiAllegato;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.AggiungiAllegatoApiTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.XMLApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.util.TaskDiFirmaUtil;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

public class AggiungiAllegatoApiTaskImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements AggiungiAllegatoApiTask {

	public AggiungiAllegatoApiTaskImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.AGGIUNGI_ALLEGATO;
	}

	@Override
	public void aggiungiAllegato(Allegato allegato, AggiungiAllegato handler) throws ApplicationException, InvalidArgumentException {
		String nomeAllegato = null;
		boolean isProtocollato = XmlPluginUtil.isAllegatoProtocollato(allegato, task.getEnclosingPratica());
		boolean isInTaskFirmaAttivo = false;
		boolean isInTaskFirma = false;
		List<XMLApprovazioneFirmaTask> taskFirma = null;

		isInTaskFirmaAttivo = TaskDiFirmaUtil.getApprovazioneFirmaTaskAttivoByAllegato(task.getEnclosingPratica(), allegato) != null;
		taskFirma = TaskDiFirmaUtil.getApprovazioneFirmaTasksByAllegato(task.getEnclosingPratica(), allegato, false, false);
		isInTaskFirma = !taskFirma.isEmpty();

		Allegato aggiungiAllegato = null;

		if (isProtocollato) {
			nomeAllegato = XmlPluginUtil.getNewNomeAllegatoProtocollatoFascicolo(allegato, task.getEnclosingPratica());
			aggiungiAllegato = handler.aggiungiAllegato(new StringBuilder().append(getDatiFascicolo().getFolderPath()).append("/").append(task.getEnclosingPratica().getSubFolderPath()).toString(),
					nomeAllegato);
			getDatiFascicolo().getAllegati().add(aggiungiAllegato);

		} else if (Boolean.FALSE.equals(allegato.getVersionable()) || Boolean.TRUE.equals(allegato.getLock()) || isInTaskFirmaAttivo) {
			nomeAllegato = XmlPluginUtil.getNewNomeAllegatoFascicolo(allegato, task.getEnclosingPratica());
			aggiungiAllegato = handler.aggiungiAllegato(new StringBuilder().append(getDatiFascicolo().getFolderPath()).append("/").append(task.getEnclosingPratica().getSubFolderPath()).toString(),
					nomeAllegato);
			getDatiFascicolo().getAllegati().add(aggiungiAllegato);

		} else {

			/*
			 * Se esiste già un allegato con lo stesso nome, questo verrà sovrascritto e perderà ovviamente tutte le informazioni conservate nello storico versioni (come ad es. le informazioni del
			 * task di firma). Dunque, se l'allegato era in un task di firma, questo viene invalidato per evitare che le informazioni del task puntino al nuovo allegato.
			 */
			if (isInTaskFirma && taskFirma != null) {
				for (XMLApprovazioneFirmaTask task : taskFirma)
					if (!Boolean.TRUE.equals(task.getDati().getAttivo()))
						task.invalida();
			}

			nomeAllegato = allegato.getNome();
			aggiungiAllegato = handler.aggiungiAllegato(new StringBuilder().append(getDatiFascicolo().getFolderPath()).append("/").append(task.getEnclosingPratica().getSubFolderPath()).toString(),
					nomeAllegato);
			if (XmlPluginUtil.allegatoInPratica(aggiungiAllegato, task.getEnclosingPratica()).size() != 0)
				getDatiFascicolo().getAllegati().removeAll(XmlPluginUtil.allegatoInPratica(aggiungiAllegato, task.getEnclosingPratica()));
			getDatiFascicolo().getAllegati().add(aggiungiAllegato);
		}

		generaEvento(EventiIterFascicolo.CARICA_ALLEGATO, task.getCurrentUser(), allegato.getLabel());
	}
}
