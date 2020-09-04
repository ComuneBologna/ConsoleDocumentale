package it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni;

import it.eng.consolepec.xmlplugin.factory.DatiPratica.Utente;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.ITaskApi;

public interface GestionePresaInCaricoApiTask extends ITaskApi {

	public void prendiInCarico(Utente user);

	public void rilasciaInCarico(Utente user);

}
