package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import java.util.List;

import it.eng.consolepec.xmlplugin.factory.Pratica;

public interface RitornaDaInoltrareEsternoTaskApi extends ITaskApi {

	public void ritornaDaInoltrareEsterno(List<Pratica<?>> praticheCollegate);
}
