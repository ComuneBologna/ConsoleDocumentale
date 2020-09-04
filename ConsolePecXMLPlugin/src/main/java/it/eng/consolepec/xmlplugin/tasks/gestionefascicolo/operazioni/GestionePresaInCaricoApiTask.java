package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import it.eng.consolepec.xmlplugin.factory.DatiPratica.Utente;

public interface GestionePresaInCaricoApiTask extends ITaskApi {

	public void prendiInCarico(Utente user);

	public void rilasciaInCarico(Utente user);

}
