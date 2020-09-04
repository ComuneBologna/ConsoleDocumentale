package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

/**
 * @author GiacomoFM
 * @since 16/nov/2017
 */
public interface CollegaPraticaProcediTaskApi extends ITaskApi {

	public boolean collegaPraticaProcedi(String idPraticaProcedi);
	
	public boolean eliminaCollegaPraticaProcedi(String idPraticaProcedi);

}
