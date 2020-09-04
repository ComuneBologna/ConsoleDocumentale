package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl;

import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.DatiGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.XMLGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.MancataConsegnaReinoltroTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;

public class MancataConsegnaReinoltroPECApiImpl<T extends DatiGestionePECTask> extends AbstractTaskPECApiImpl<T> implements MancataConsegnaReinoltroTaskPECApi {

	public MancataConsegnaReinoltroPECApiImpl(XMLGestionePECTask task) {
		super(task);
	}

	@Override
	public void impostaMancataConsegnaInReinoltro() {
		task.getEnclosingPratica().getDati().setStato(Stato.MANCATA_CONSEGNA_IN_REINOLTRO);
	}
	
	@Override
	protected TipoApiTaskPEC getTipoApiTask() {
		return TipoApiTaskPEC.MANCATA_CONSEGNA_REINOLTRO;
	}
	
	@Override
	protected boolean controllaAbilitazioneInterna() {
		return Stato.MANCATA_CONSEGNA.equals(task.getEnclosingPratica().getDati().getStato());
	}

	

}
