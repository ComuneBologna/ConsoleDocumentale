package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import java.util.List;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.GruppoVisibilita;

public interface CambiaVisibilitaFascicoloTaskApi extends ITaskApi {

	public void rimuoviVisibilitaFascicolo(List<AnagraficaRuolo> gruppiDaRimuovere) throws PraticaException;

	public void aggiungiVisibilitaFascicolo(List<AnagraficaRuolo> gruppiDaAggiungere) throws PraticaException;

	public void aggiungiVisibilita(List<GruppoVisibilita> gruppiDaAggiungere) throws PraticaException;

}
