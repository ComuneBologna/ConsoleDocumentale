package it.eng.consolepec.xmlplugin.tasks.gestionepec;

import it.eng.consolepec.xmlplugin.pratica.operazionipecconcluse.AnnullaElettoraleApiTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.RiportaInLetturaTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.AgganciaAPECTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.ArchiviaTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.EliminaTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.ImportaElettoraleTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.ModificaOperatoreTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.NotificaTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.RiconsegnaTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.RispostaInteroperabileTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.ScartaTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.operazioni.ModificaNoteTaskApi;

public interface GestionePECInTask extends GestionePECTask, ArchiviaTaskPECApi, EliminaTaskPECApi, RiconsegnaTaskPECApi, RiportaInLetturaTaskApi, ScartaTaskPECApi, NotificaTaskPECApi, AgganciaAPECTaskPECApi, RispostaInteroperabileTaskApi, ImportaElettoraleTaskPECApi, AnnullaElettoraleApiTask, ModificaOperatoreTaskPECApi, ModificaNoteTaskApi {

}
