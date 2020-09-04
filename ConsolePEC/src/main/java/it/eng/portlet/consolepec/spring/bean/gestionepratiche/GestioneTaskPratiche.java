package it.eng.portlet.consolepec.spring.bean.gestionepratiche;

import java.util.List;
import java.util.Set;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailIn;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailOut;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.pratica.modulistica.PraticaModulistica;
import it.eng.consolepec.xmlplugin.tasks.riattiva.RiattivaFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.riattiva.RiattivaPECInTask;
import it.eng.consolepec.xmlplugin.tasks.riattiva.RiattivaPECOutTask;
import it.eng.consolepec.xmlplugin.tasks.riattiva.RiattivaPraticaModulisticaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.ApprovazioneFirmaTask;

public interface GestioneTaskPratiche {

	public <T extends Task<?>> T estraiTaskCorrente(Pratica<?> pratica, Class<T> clazz, List<AnagraficaRuolo> anagraficheRuoli);

	public RiattivaPECOutTask estraiTaskRiattivazione(PraticaEmailOut email);

	public Task<?> estraiTaskCorrente(Pratica<?> pratica, List<AnagraficaRuolo> anagraficheRuoli);

	public Task<?> estraiTaskCorrente(Pratica<?> pratica);

	public RiattivaPECInTask getRiattivaPecInTask(PraticaEmailIn email);

	public RiattivaFascicoloTask getRiattivaFascicoloTask(Fascicolo fascicolo);

	public RiattivaPraticaModulisticaTask getRiattivaPraticaModulisticaTask(PraticaModulistica praticaModulistica);

	public <T extends Task<?>> T estraiTaskCorrenteSoloAssegnatario(Pratica<?> pratica, Class<T> clazz);

	public Task<?> estraiTaskCorrenteSoloAssegnatario(Pratica<?> pratica);

	public Set<ApprovazioneFirmaTask> getAllTaskFirma(Pratica<?> pratica);

}
