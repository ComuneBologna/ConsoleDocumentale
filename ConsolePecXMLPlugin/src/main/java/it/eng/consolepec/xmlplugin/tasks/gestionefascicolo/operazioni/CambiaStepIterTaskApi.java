package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import java.util.List;


public interface CambiaStepIterTaskApi extends ITaskApi {

	public void cambiaStep(String step, boolean finale, boolean iniziale, boolean creaBozza, List<String> destinatariNotifica);

	public void cambiaStep(String step, boolean finale, boolean iniziale, boolean creaBozza);

}
