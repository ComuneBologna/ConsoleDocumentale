package it.eng.consolepec.xmlplugin.test.api;

import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.TaskFascicolo;

import java.util.Set;

public class FascicoloApiTest {
	
	public TaskFascicolo<?> getTaskCorrente(Fascicolo fascicolo){
		Set<Task<?>> tasks = fascicolo.getTasks();
		for(Task<?> t: tasks){
			if(t instanceof TaskFascicolo && t.isAttivo())
				return (TaskFascicolo<?>) t;
		}
		return null;
	}

}
