package it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.impl;

import it.eng.consolepec.xmlplugin.pratica.template.DatiAbstractTemplate;
import it.eng.consolepec.xmlplugin.tasks.eventiiter.EventiIter;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.ITaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.DatiGestioneAbstractTemplateTask;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.XMLGestioneAbstractTemplateTask;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.TipoApiTaskTemplate;
import it.eng.consolepec.xmlplugin.tasks.operazioni.AbstractTaskApi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTemplateTaskApiImpl<T extends DatiGestioneAbstractTemplateTask> extends AbstractTaskApi implements ITaskApi {

	protected Logger log = LoggerFactory.getLogger(AbstractTemplateTaskApiImpl.class);

	protected XMLGestioneAbstractTemplateTask<T> task;

	public AbstractTemplateTaskApiImpl(XMLGestioneAbstractTemplateTask<T> task) {
		super(task);
		this.task = task;
	}

	
	protected DatiAbstractTemplate getDatiTemplate() {
		return (DatiAbstractTemplate) task.getEnclosingPratica().getDati();
	}

	
	protected abstract TipoApiTaskTemplate getTipoApiTask();

	
	
	public static enum EventiIterTemplate implements EventiIter {

		//
		MODIFICA(false, "L''utente {0} ha modificato il template."),
		//
		PRENDI_IN_CARICO(false, "L''utente {0} ha preso in carico il template."),
		//
		RILASCIA_IN_CARICO(false, "L''utente {0} ha rilasciato il template."), 
		//
		CARICA_ALLEGATO(false, "L''utente {0} ha caricato l''allegato {1}."), 
		//
		ELIMINA_ALLEGATO(false, "L''utente {0} ha eliminato l''allegato {1}."),
		//
		CARICA_ODT(false, "L''utente {0} ha caricato il modello {1}.");

		
		private EventiIterTemplate(boolean isSerializzazioneAbilitata, String testoDaSerializzare) {
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
