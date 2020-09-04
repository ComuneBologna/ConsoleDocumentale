package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni;

import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.ITaskApi;

import java.util.List;


public interface FirmaTaskPECApi extends ITaskApi{
	
	public void firmaAllegati(List<Allegato> allegati);

}
