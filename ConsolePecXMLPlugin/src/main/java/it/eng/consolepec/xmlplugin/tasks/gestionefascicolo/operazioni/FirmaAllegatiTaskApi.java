package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;

import java.util.List;

public interface FirmaAllegatiTaskApi extends ITaskApi {
	public void firmaAllegati(List<Allegato> allegati) throws PraticaException;
}
