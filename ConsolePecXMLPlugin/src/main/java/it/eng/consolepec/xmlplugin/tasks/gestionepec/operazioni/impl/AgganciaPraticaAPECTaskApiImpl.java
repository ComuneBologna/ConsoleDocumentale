package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmail;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.DatiGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.XMLGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.AgganciaPraticaAPECTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;

public class AgganciaPraticaAPECTaskApiImpl<T extends DatiGestionePECTask> extends AbstractTaskPECApiImpl<T> implements AgganciaPraticaAPECTaskApi {

	public AgganciaPraticaAPECTaskApiImpl(XMLGestionePECTask task) {
		super(task);
	}

	@Override
	public void agganciaPraticaAPEC(Pratica<?> praticaDaAggiungere) throws PraticaException {
		if (praticaDaAggiungere == null)
			throw new PraticaException("La pratica da collegare è null");
				
		PraticaEmail emailCorrente = task.getEnclosingPratica();
		
		if(emailCorrente.hasPraticaCollegata(praticaDaAggiungere))
			throw new PraticaException("La pratica è già collegata");
		
		it.eng.consolepec.xmlplugin.pratica.email.DatiEmail dati = emailCorrente.getDati();
		emailCorrente.addPraticaCollegata(dati.new PraticaCollegata(praticaDaAggiungere.getAlfrescoPath(), praticaDaAggiungere.getDati().getTipo().getNomeTipologia(), praticaDaAggiungere.getDati().getDataCreazione()));
		
		DatiPratica datiPraticaDaAggiungere = praticaDaAggiungere.getDati();
		praticaDaAggiungere.addPraticaCollegata(datiPraticaDaAggiungere.new PraticaCollegata(emailCorrente.getAlfrescoPath(), emailCorrente.getDati().getTipo().getNomeTipologia(), emailCorrente.getDati().getDataCreazione()));
		
		generaEvento(EventiIterPEC.AGGANCIA_A_PRATICA, task.getCurrentUser(), praticaDaAggiungere.getDati().getIdDocumentale());
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}

	@Override
	protected TipoApiTaskPEC getTipoApiTask() {
		return TipoApiTaskPEC.AGGANCIA_PRATICA;
	}

}
