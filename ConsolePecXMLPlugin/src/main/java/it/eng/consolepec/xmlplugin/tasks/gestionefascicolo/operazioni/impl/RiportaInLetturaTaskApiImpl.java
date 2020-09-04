package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.RiportaInLetturaTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;

public class RiportaInLetturaTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements RiportaInLetturaTaskApi {

	public RiportaInLetturaTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}
	
	@Override
	public void riportaInLettura() throws PraticaException {
		getDatiFascicolo().setLetto(false);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return getDatiFascicolo().isLetto() == true;
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.RIPORTA_IN_LETTURA;
	}

}
