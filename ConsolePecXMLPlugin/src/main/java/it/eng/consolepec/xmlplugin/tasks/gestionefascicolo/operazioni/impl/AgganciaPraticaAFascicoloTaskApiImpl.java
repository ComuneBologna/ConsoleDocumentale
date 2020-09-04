package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.AgganciaPraticaAFascicoloTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;

public class AgganciaPraticaAFascicoloTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements AgganciaPraticaAFascicoloTaskApi {

	public AgganciaPraticaAFascicoloTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	public void agganciaPraticaAFascicolo(Pratica<?> praticaDaAggiungere) throws PraticaException {
		if (praticaDaAggiungere == null)
			throw new PraticaException("La pratica da collegare è null");

		Fascicolo fascicolo = (Fascicolo) task.getEnclosingPratica();

		if (fascicolo.hasPraticaCollegata(praticaDaAggiungere))
			throw new PraticaException("La pratica è già collegata");

		DatiFascicolo datiFascicolo = fascicolo.getDati();
		fascicolo.addPraticaCollegata(datiFascicolo.new PraticaCollegata(praticaDaAggiungere.getAlfrescoPath(), praticaDaAggiungere.getDati().getTipo().getNomeTipologia(),
				praticaDaAggiungere.getDati().getDataCreazione()));

		DatiPratica datiPratica = praticaDaAggiungere.getDati();
		praticaDaAggiungere.addPraticaCollegata(datiPratica.new PraticaCollegata(fascicolo.getAlfrescoPath(), fascicolo.getDati().getTipo().getNomeTipologia(), datiFascicolo.getDataCreazione()));

		if (task.controllaAbilitazione(TipoApiTask.RIPORTA_IN_LETTURA))
			task.riportaInLettura();

		generaEvento(EventiIterFascicolo.AGGANCIA_A_PRATICA, task.getCurrentUser(), praticaDaAggiungere.getDati().getTipo().getEtichettaTipologia(), praticaDaAggiungere.getDati().getIdDocumentale());
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.AGGANCIA_PRATICA_A_FASCICOLO;
	}

}
