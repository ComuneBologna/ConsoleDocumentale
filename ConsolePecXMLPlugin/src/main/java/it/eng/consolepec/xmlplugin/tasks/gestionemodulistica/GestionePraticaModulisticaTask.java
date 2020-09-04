package it.eng.consolepec.xmlplugin.tasks.gestionemodulistica;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.cobo.consolepec.commons.exception.InvalidArgumentException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.factory.TaskObserver.AggiungiAllegato;

public interface GestionePraticaModulisticaTask extends Task<DatiGestionePraticaModulisticaTask> {

	public void archivia();

	public boolean isArchiviaAbilitato();

	public boolean isEliminaAbilitato();

	public boolean isCreaFascicoloAbilitato();

	public void aggiungiAllegato(Allegato allegato, AggiungiAllegato handler) throws ApplicationException, InvalidArgumentException;

	public boolean isRiassegnaAbilitato();

	public void riassegna(AnagraficaRuolo nuovoAssegnatario, String operatore);

	public boolean isAggiungiFascicoloAbilitato();
}
