package it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.operazioni.impl;

import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.cobo.consolepec.commons.exception.InvalidArgumentException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.TaskObserver.AggiungiAllegato;
import it.eng.consolepec.xmlplugin.pratica.comunicazione.DatiComunicazione.Stato;
import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.DatiGestioneComunicazioneTask;
import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.XMLGestioneComunicazioneTask;
import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.operazioni.AggiungiAllegatoComunicazioneTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.operazioni.TipoApiTaskComunicazione;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

public class AggiungiAllegatoComunicazioneTaskApiImpl<T extends DatiGestioneComunicazioneTask> extends AbstractComunicazioneTaskApiImpl<T> implements AggiungiAllegatoComunicazioneTaskApi {

	public AggiungiAllegatoComunicazioneTaskApiImpl(XMLGestioneComunicazioneTask task) {
		super(task);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return Stato.IN_GESTIONE.equals(getDatiComunicazione().getStato());
	}

	@Override
	protected TipoApiTaskComunicazione getTipoApiTask() {
		return TipoApiTaskComunicazione.AGGIUNGI_ALLEGATO;
	}

	@Override
	public void aggiungiAllegato(Allegato allegato, AggiungiAllegato handler) throws ApplicationException, InvalidArgumentException {

		String nomeAllegato = allegato.getNome();
		Allegato aggiungiAllegato = handler.aggiungiAllegato(
				new StringBuilder().append(getDatiComunicazione().getFolderPath()).append("/").append(task.getEnclosingPratica().getSubFolderPath()).toString(), nomeAllegato);

		if ("csv".equalsIgnoreCase(XmlPluginUtil.getExtension(aggiungiAllegato))) {
			aggiungiAllegato.getTipologiaDocumento().add(XmlPluginUtil.INVIABILE);
		}

		getDatiComunicazione().getAllegati().removeAll(XmlPluginUtil.allegatoInPratica(aggiungiAllegato, task.getEnclosingPratica()));
		getDatiComunicazione().getAllegati().add(aggiungiAllegato);

		generaEvento(EventiIterComunicazione.CARICA_ALLEGATO, task.getCurrentUser(), allegato.getLabel());
	}

}
