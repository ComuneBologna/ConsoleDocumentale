package it.eng.consolepec.xmlplugin.tasks.eventiiter;

import it.eng.consolepec.xmlplugin.factory.DatiPratica.EventoIterPratica;
import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

/**
 * 
 * Classe interna è usata per la serializzazione degli iter di una pratica
 * 
 * l'array di parametri viene utilizzato per valorizzare i placeholder del testo
 * 
 * @author roger
 * 
 */
public class SerializzatoreEventiIter {
		
		private Task<?> task;
		private EventiIter eventiIter;
		private Object[] params;

		public SerializzatoreEventiIter(Task<?> task, EventiIter eventiIter, Object... params) {
			super();
			this.task = task;
			this.eventiIter = eventiIter;
			this.params = params;
		}

		public void serializeIter() {
			serializeIter(null, null);
		}

		public void serializeIter(String numeroPG, Integer annoPG) {

			
			if (task.getCurrentUser() == null)
				throw new IllegalStateException("Si deve specificare l'utente che esegue l'operazione");

			String iterMessage = XmlPluginUtil.format(eventiIter.getTestoDaSerializzare(), params);
			EventoIterPratica eventoIterPratica = task.getEnclosingPratica().getDati().new EventoIterPratica();
			eventoIterPratica.setSerialized(false);
			eventoIterPratica.setSerializationEnabled(eventiIter.isSerializzazioneAbilitata());
			eventoIterPratica.setTestoEvento(iterMessage);
			eventoIterPratica.setTipoEvento(eventiIter.name());
			eventoIterPratica.setAnnpPG(annoPG);
			eventoIterPratica.setNumeroPG(numeroPG);
			eventoIterPratica.setUser(task.getCurrentUser());
			task.getEnclosingPratica().getDati().getIter().add(eventoIterPratica);
		
		}

		
	}