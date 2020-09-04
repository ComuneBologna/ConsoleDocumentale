package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailIn;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.SganciaPecInApiTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl.AbstractTaskPECApiImpl.EventiIterPEC;
import it.eng.consolepec.xmlplugin.tasks.riattiva.RiattivaPECInTask;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

public class SganciaPecInApiTaskImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements SganciaPecInApiTask {

	public SganciaPecInApiTaskImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.SGANCIA_PEC_IN;
	}

	@Override
	public void sganciaPecIn(PraticaEmailIn pecIn) throws PraticaException {
		//Accedo ai dati del fascicolo ed elimino la PecIn
		Fascicolo fascicolo = (Fascicolo) task.getEnclosingPratica();
		fascicolo.removePraticaCollegata(pecIn);
	
		//La scollego dal fascicolo
		pecIn.removePraticaCollegata(fascicolo);
				
		//Riattivo il task della PecIn rimossa dal fascicolo in modo da riabilitare tutte le operazioni disabilitate in precedenza con l'aggiunta al fascicolo
		if(!pecIn.isAttiva()){
			
			RiattivaPECInTask taskRiattiva = XmlPluginUtil.getRiattivaTaskCorrente(pecIn, RiattivaPECInTask.class, task.getCurrentUser());
			taskRiattiva.riattivaTaskAssociato();
		}
		
		//Aggiungo nella sezione iter della pratica le operazioni compiute dell'utente (lo storico).
		generaEvento( EventiIterFascicolo.SGANCIA_PEC_IN, task.getCurrentUser(), pecIn.getDati().getIdDocumentale(), fascicolo.getDati().getIdDocumentale());
		generaEvento( EventiIterPEC.SGANCIA_PEC_IN, task.getCurrentUser(), pecIn.getDati().getIdDocumentale(), fascicolo.getDati().getIdDocumentale());
	}

}