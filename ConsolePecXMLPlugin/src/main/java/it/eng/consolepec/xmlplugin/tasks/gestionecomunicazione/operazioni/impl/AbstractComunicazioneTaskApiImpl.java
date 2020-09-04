package it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.operazioni.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.consolepec.xmlplugin.pratica.comunicazione.DatiComunicazione;
import it.eng.consolepec.xmlplugin.tasks.eventiiter.EventiIter;
import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.DatiGestioneComunicazioneTask;
import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.XMLGestioneComunicazioneTask;
import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.operazioni.TipoApiTaskComunicazione;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.ITaskApi;
import it.eng.consolepec.xmlplugin.tasks.operazioni.AbstractTaskApi;

public abstract class AbstractComunicazioneTaskApiImpl<T extends DatiGestioneComunicazioneTask> extends AbstractTaskApi implements ITaskApi {

	protected Logger log = LoggerFactory.getLogger(AbstractComunicazioneTaskApiImpl.class);

	protected XMLGestioneComunicazioneTask task;

	public AbstractComunicazioneTaskApiImpl(XMLGestioneComunicazioneTask task) {
		super(task);
		this.task = task;
	}

	protected DatiComunicazione getDatiComunicazione() {
		return task.getEnclosingPratica().getDati();
	}

	@Override
	protected abstract TipoApiTaskComunicazione getTipoApiTask();

	public static enum EventiIterComunicazione implements EventiIter {

		//
		CARICA_ALLEGATO(false, "L''utente {0} ha caricato l''allegato {1}."),
		//
		ELIMINA_ALLEGATO(false, "L''utente {0} ha eliminato l''allegato {1}."),
		//
		INVIATA(false, "L''utente {0} ha inviato la comunicazione."),
		//
		INVIATA_TEST(false, "L''utente {0} ha inviato una comunicazione di test.");

		private EventiIterComunicazione(boolean isSerializzazioneAbilitata, String testoDaSerializzare) {
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
