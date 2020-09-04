package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl;

import java.util.List;

import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.cobo.consolepec.commons.exception.InvalidArgumentException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.factory.TaskObserver.AggiungiAllegato;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.DatiGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.XMLGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.AggiungiAllegatoTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

public class AggiungiAllegatoTaskPECApiImpl<T extends DatiGestionePECTask> extends AbstractTaskPECApiImpl<T> implements AggiungiAllegatoTaskPECApi {

	public AggiungiAllegatoTaskPECApiImpl(XMLGestionePECTask task) {
		super(task);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return Stato.BOZZA.equals(task.getEnclosingPratica().getDati().getStato());
	}

	@Override
	public void aggiungiAllegato(Allegato allegato, AggiungiAllegato handler) throws ApplicationException, InvalidArgumentException {
		Allegato newAllegato = handler.aggiungiAllegato(task.getEnclosingPratica().getDati().getFolderPath() + "/", allegato.getNome());
		List<Allegato> allegatiInPratica = XmlPluginUtil.allegatoInPratica(newAllegato, task.getEnclosingPratica());

		if (allegatiInPratica.size() != 0) {
			task.getEnclosingPratica().getDati().getAllegati().removeAll(allegatiInPratica);
		}

		task.getEnclosingPratica().getDati().getAllegati().add(newAllegato);

	}

	@Override
	public void aggiungiAllegato(Allegato allegato, Pratica<?> pratica, AggiungiAllegato handler) throws ApplicationException, InvalidArgumentException {

		Allegato newAllegato = handler.aggiungiAllegato(pratica.getDati().getFolderPath() + "/" + pratica.getSubFolderPath() + "/", allegato.getNome());
		List<Allegato> allegatiInPratica = XmlPluginUtil.allegatoInPratica(newAllegato, task.getEnclosingPratica());
		if (allegatiInPratica.size() != 0) {
			task.getEnclosingPratica().getDati().getAllegati().removeAll(allegatiInPratica);
		}
		task.getEnclosingPratica().getDati().getAllegati().add(newAllegato);
		newAllegato.getGruppiVisibilita().addAll(task.getEnclosingPratica().getDati().getGruppiVisibilita());

	}

	@Override
	protected TipoApiTaskPEC getTipoApiTask() {
		return TipoApiTaskPEC.AGGIUNGI_ALLEGATO;
	}

}
