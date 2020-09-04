package it.eng.consolepec.xmlplugin.tasks.richiestafirma.operazioni;

import it.eng.consolepec.xmlplugin.tasks.eventiiter.EventiIter;
import it.eng.consolepec.xmlplugin.tasks.operazioni.AbstractTaskApi;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.XMLApprovazioneFirmaTask;

public abstract class AbstractApprovazioneFirmaTaskApi extends AbstractTaskApi {

	protected XMLApprovazioneFirmaTask task;
	
	public AbstractApprovazioneFirmaTaskApi(XMLApprovazioneFirmaTask task) {
		super(task);
		this.task = task;
	}
	
	@Override
	public boolean isOperazioneAbilitata(){
		
		return controllaAbilitazioneUtente() && controllaAbilitazioneGenerica() && controllaAbilitazioneInterna();
	}
	
	@Override
	protected boolean controllaAbilitazioneUtente(){
		return true; 
	}
	
	@Override
	protected boolean controllaAbilitazioneGenerica() {
		return true;
	}
	
	
	public static enum EventiIterApprovazioneFirma implements EventiIter {

		GESTIONE_APPROVAZIONE_FIRMA(false,"L''utente {0} ha aggiornato la proposta di approvazione dell''allegato {1}. Operazione eseguita: {2}"),
		GESTIONE_APPROVAZIONE_FIRMA_CON_NOTIFICA(false,"L''utente {0} ha aggiornato la proposta di approvazione dell''allegato {1}. Operazione eseguita: {2}. La notifica verra'' inviata a: {3}.");
		
		private EventiIterApprovazioneFirma(boolean isSerializzazioneAbilitata, String testoDaSerializzare) {
			this.isSerializzazioneAbilitata = isSerializzazioneAbilitata;
			this.testoDaSerializzare = testoDaSerializzare;
		}

		private boolean isSerializzazioneAbilitata;
		private String testoDaSerializzare;

		@Override
		public boolean isSerializzazioneAbilitata() {
			return isSerializzazioneAbilitata;
		}

		@Override
		public String getTestoDaSerializzare() {
			return testoDaSerializzare;
		}

	}

}
