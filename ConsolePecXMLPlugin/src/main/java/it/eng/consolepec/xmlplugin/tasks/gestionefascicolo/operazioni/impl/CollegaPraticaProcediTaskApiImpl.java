package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.consolepec.xmlplugin.factory.ITipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.CollegaPraticaProcediTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;

import java.util.ArrayList;
import java.util.List;

public class CollegaPraticaProcediTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements CollegaPraticaProcediTaskApi {

	public CollegaPraticaProcediTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}
	
	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}
	
	@Override
	protected ITipoApiTask getTipoApiTask() {
		return TipoApiTask.COLLEGA_PRATICA_PROCEDI;
	}

	@Override
	public boolean collegaPraticaProcedi(String idPraticaProcedi) {
		
		if (getDatiFascicolo().getIdPraticheProcedi() != null) {
			
			if (!getDatiFascicolo().getIdPraticheProcedi().contains(idPraticaProcedi)) {
				getDatiFascicolo().getIdPraticheProcedi().add(idPraticaProcedi);
				
			} else {
				return false;
			}
			
		} else {
			List<String> ids = new ArrayList<String>();
			ids.add(idPraticaProcedi);
		}
				
		generaEvento(EventiIterFascicolo.COLLEGA_PRATICA_PROCEDI, task.getCurrentUser(), idPraticaProcedi);
		return true;
	}
	
	@Override
	public boolean eliminaCollegaPraticaProcedi(String idPraticaProcedi) {
		
		if (getDatiFascicolo().getIdPraticheProcedi() != null) {
			
			if (getDatiFascicolo().getIdPraticheProcedi().contains(idPraticaProcedi)) {
				getDatiFascicolo().getIdPraticheProcedi().remove(idPraticaProcedi);
				
			} else {
				return false;
			}
			
		} else {
			return false;
		}		
		
		generaEvento(EventiIterFascicolo.ELIMINA_COLLEGA_PRATICA_PROCEDI, task.getCurrentUser(), idPraticaProcedi);
		return true;
	}
}
