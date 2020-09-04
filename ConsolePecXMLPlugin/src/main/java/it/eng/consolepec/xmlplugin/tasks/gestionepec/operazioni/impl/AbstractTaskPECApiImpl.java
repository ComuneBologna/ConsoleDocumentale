package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail;
import it.eng.consolepec.xmlplugin.tasks.eventiiter.EventiIter;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.ITaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.DatiGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.XMLGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;
import it.eng.consolepec.xmlplugin.tasks.operazioni.AbstractTaskApi;

public abstract class AbstractTaskPECApiImpl<T extends DatiGestionePECTask> extends AbstractTaskApi implements ITaskApi {

	protected Logger log = LoggerFactory.getLogger(AbstractTaskPECApiImpl.class);

	protected XMLGestionePECTask task;

	public AbstractTaskPECApiImpl(XMLGestionePECTask task) {
		super(task);
		this.task = task;
	}

	protected DatiEmail DatiEmail() {
		return task.getEnclosingPratica().getDati();
	}

	@Override
	protected abstract TipoApiTaskPEC getTipoApiTask();

	public static enum EventiIterPEC implements EventiIter {

		ARCHIVIA(true, "L''utente {0} ha archiviato la mail"),
		//
		ELIMINA(true, "L''utente {0} ha eliminato la mail"),
		//
		RIASSEGNA(true, "L''utente {0} ha riassegnato l''email da {1} a {2}."),
		//
		RIASSEGNA_CON_NOTIFICA(true, "L''utente {0} ha riassegnato l''email da {1} a {2}. La notifica verra'' inviata a: {3}."),
		//
		CREA_FASCICOLO(true, "L''utente {0} ha creato un nuovo fascicolo."),
		//
		AGGIUNGI_FASCICOLO(true, "L''utente {0} ha aggiunto un fascicolo esistente alla mail"),
		//
		RICONSEGNA(true, "Il sistema ha notificato a Eprotocollo la riconsegna della mail {0}"),
		//
		SCARTA(true, "L''utente {0} ha scartato la mail"),
		//
		NOTIFICA(true, "Il sistema ha gestito questa email come interoperabile"),
		//
		AGGANCIA_A_PRATICA(true, "L''utente {0} ha agganciato l''email corrente con l''email {1}."),
		//
		RISPOSTA_INTEROPERABILE(true, "Risposta al messaggio interoperabile {0}. Tipo risposta: {1}."),
		//
		SGANCIA_PEC_IN(true, "L''utente {0} ha eliminato l''e-mail {1} dal fascicolo {2}."),
		//
		MODIFICA_OPERATORE(true, "L''utente {0} ha attribuito l''email all''operatore {1}."),
		//
		ELIMINA_ALLEGATO(false, "L''utente {0} ha eliminato l''allegato {1}."),
		//
		INVIA_PEC(false, "L''utente {0} ha inviato la pec."),
		//
		PEC_IN_ATTESA_PRESA_IN_CARICO(false, "La pec è stata presa in carico dal server."),
		//
		PEC_INVIATA(false, "La pec è stata inviata."),
		//
		PEC_NON_INVIATA(false, "La pec non è stata inviata."),
		//
		MODIFICA_BOZZA(false, "L''utente {0} ha modificato la bozza; Modifiche: {1}."),
		//
		MODIFICA_NOTE(false, "L''utente {0} ha modificato le note."),
		//
		AGGIUNTA_NOTE(false, "L''utente {0} ha aggiunto delle note.");

		private EventiIterPEC(boolean isSerializzazioneAbilitata, String testoDaSerializzare) {
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
