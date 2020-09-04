package it.eng.consolepec.xmlplugin.pratica.operazionipecconcluse;

import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.ITaskApi;

import java.util.List;

public interface AnnullaElettoraleApiTask extends ITaskApi{
	
	 void annullaElettorale(List<Fascicolo> fascicoliCollegati, List<Fascicolo> daArchiviare);

}
