package it.eng.consolepec.xmlplugin.tasks.richiestafirma;

import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.AggiungiAllegatoApiTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.EvadiApprovazioneFirmaTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.FirmaAllegatiTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.InvalidaApprovazioneFirmaTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.RiassegnaFascicoloTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.VersionaAllegatoTaskFirmaApiTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.operazioni.GestioneApprovazioneFirmaApiTask;

public interface ApprovazioneFirmaTask extends Task<DatiApprovazioneFirmaTask>, FirmaAllegatiTaskApi, AggiungiAllegatoApiTask, RiassegnaFascicoloTaskApi, VersionaAllegatoTaskFirmaApiTask, GestioneApprovazioneFirmaApiTask, EvadiApprovazioneFirmaTaskApi, InvalidaApprovazioneFirmaTaskApi {

}
