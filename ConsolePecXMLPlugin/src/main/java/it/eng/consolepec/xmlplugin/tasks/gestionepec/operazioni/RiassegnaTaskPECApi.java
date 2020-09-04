package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni;

import java.util.List;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.GruppoVisibilita;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.ITaskApi;

public interface RiassegnaTaskPECApi extends ITaskApi {

	public void riassegna(AnagraficaRuolo nuovoAssegnatario, List<GruppoVisibilita> gruppiVisibilita, String operatore, List<String> indirizziNotifica);

}
