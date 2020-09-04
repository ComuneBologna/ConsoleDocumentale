package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.consolepec.xmlplugin.factory.DatiPratica.Operazione;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Stato;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.ModificaAbilitazioniAssegnaUtenteEsternoTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;

import java.util.Set;

public class ModificaAbilitazioniAssegnaUtenteEsternoTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements ModificaAbilitazioniAssegnaUtenteEsternoTaskApi {

	public ModificaAbilitazioniAssegnaUtenteEsternoTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	
	@Override
	protected boolean controllaAbilitazioneInterna() {
		return getDatiFascicolo().getStato().equals(Stato.DA_INOLTRARE_ESTERNO);
	}

	
	@Override
	protected boolean controllaAbilitazioneUtente() {
		if(task.isUtenteEsterno()){
			return false;
		} 
		return super.controllaAbilitazioneUtente();
	}
	
	@Override
	protected boolean controllaAbilitazioneGenerica() {
		return true;
	}
	
	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.MODIFICA_ABILITAZIONI_ASSEGNA_UTENTE_ESTERNO;
	}

	

	@Override
	public void modificaAbilitazioni(Set<String> operazioni) {
		getDatiFascicolo().getAssegnazioneEsterna().getOperazioniConsentite().clear();
		
		for (String nomeOperazione : operazioni) {
			for (Operazione op : getDatiFascicolo().getOperazioni()) {
				if (op.getNomeOperazione().equalsIgnoreCase(nomeOperazione) && op.isAbilitata())
					getDatiFascicolo().getAssegnazioneEsterna().getOperazioniConsentite().add(new Operazione(nomeOperazione, true));
			}
		}
		
		generaEvento(EventiIterFascicolo.MODIFICA_ABILITAZIONI_ASSEGNA_UTENTE_ESTERNO, task.getCurrentUser());
		
	}

}
