package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.ITipoApiTask;
import it.eng.consolepec.xmlplugin.factory.TaskObserver.AggiungiAllegato;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.VersionaAllegatoTaskFirmaApiTask;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

/**
 *
 * @author biagiot
 *
 */
public class VersionaAllegatoTaskFirmaApiTaskImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements VersionaAllegatoTaskFirmaApiTask {

	public VersionaAllegatoTaskFirmaApiTaskImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	public void versionaAllegato(Allegato allegato, boolean protocollato, AggiungiAllegato handler) throws Exception {

		if (protocollato && !XmlPluginUtil.isAllegatoProtocollato(allegato, this.task.getEnclosingPratica())) {
			throw new PraticaException("L'allegato non è protocollato");
		}

		String nomeAllegato = allegato.getNome();
		Allegato allegatoVersionato = handler.aggiungiAllegato(new StringBuilder().append(getDatiFascicolo().getFolderPath()).append("/").append(task.getEnclosingPratica().getSubFolderPath()).toString(), nomeAllegato);

		if (XmlPluginUtil.allegatoInPratica(allegatoVersionato, task.getEnclosingPratica()).size() != 0)
			getDatiFascicolo().getAllegati().removeAll(XmlPluginUtil.allegatoInPratica(allegatoVersionato, task.getEnclosingPratica()));

		if (protocollato)
			XmlPluginUtil.sostituisciAllegatoProtocollato(allegato, allegatoVersionato, (Fascicolo) task.getEnclosingPratica());
		else
			getDatiFascicolo().getAllegati().add(allegatoVersionato);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}

	@Override
	protected ITipoApiTask getTipoApiTask() {
		return TipoApiTask.VERSIONA_ALLEGATO_TASK_FIRMA;
	}
}
