package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.RimuoviPraticaTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;

public class RimuoviPraticaTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements RimuoviPraticaTaskApi {

	public RimuoviPraticaTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	public void removePratica(Pratica<? extends DatiPratica> pratica) throws PraticaException {
		String alfrescoPath = pratica.getAlfrescoPath();
		
		if (task.getEnclosingPratica().hasPraticaCollegata(pratica)) {
			log.debug("Trovata pratica da rimuovere {}", alfrescoPath);
			task.getEnclosingPratica().removePraticaCollegata(pratica);
			
			generaEvento(EventiIterFascicolo.ELIMINA_PRATICA_COLLEGATA, task.getCurrentUser());
			return;
		}
		
		log.warn("Pratica da rimuovere non trovata: {}", pratica.getAlfrescoPath());
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.RIMUOVI_PRATICA;
	}

}
