package it.eng.consolepec.xmlplugin.tasks.richiestafirma.operazioni.impl;

import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl.RiassegnaFascicoloTaskApiImpl;


public class RiassegnaPraticaApprovazioneFirmaTaskApiImpl extends RiassegnaFascicoloTaskApiImpl<DatiGestioneFascicoloTask> {

	public RiassegnaPraticaApprovazioneFirmaTaskApiImpl(XMLTaskFascicolo<DatiGestioneFascicoloTask> task) {
		super(task);
	}

	@Override
	public boolean isOperazioneAbilitata() {
		if (controllaAbilitazioneUtente()) {
			if (task.getCurrentUser() == null)
				throw new IllegalStateException("current user is null");

			return controllaAbilitazioneGenerica() && controllaAbilitazioneInterna();
		}
		return false;
	}
}
