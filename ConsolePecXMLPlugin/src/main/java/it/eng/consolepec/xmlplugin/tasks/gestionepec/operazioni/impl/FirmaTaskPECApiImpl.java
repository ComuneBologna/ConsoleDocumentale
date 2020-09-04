package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmail;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.DatiGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.XMLGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.FirmaTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

import java.util.List;

public class FirmaTaskPECApiImpl<T extends DatiGestionePECTask> extends AbstractTaskPECApiImpl<T> implements FirmaTaskPECApi {

	public FirmaTaskPECApiImpl(XMLGestionePECTask task) {
		super(task);
	}
	@Override
	protected boolean controllaAbilitazioneInterna() {
		PraticaEmail pratica = task.getEnclosingPratica();
		return pratica.getDati().getAllegati().size() != 0 && pratica.getDati().getStato().equals(Stato.BOZZA);
	}
	
	@Override
	public void firmaAllegati(List<Allegato> allegati) {
		List<Allegato> list = XmlPluginUtil.allegatoInPratica(allegati, task.getEnclosingPratica());
		if (list.size() == 0) {
			throw new PraticaException("L'allegato non appartiene alla pratica associata a questo task");
		}
		task.getEnclosingPratica().getDati().getAllegati().removeAll(list);
		task.getEnclosingPratica().getDati().getAllegati().addAll(allegati);	
	}

	
	@Override
	protected TipoApiTaskPEC getTipoApiTask() {
		return TipoApiTaskPEC.FIRMA;
	}

	
	

	

}
