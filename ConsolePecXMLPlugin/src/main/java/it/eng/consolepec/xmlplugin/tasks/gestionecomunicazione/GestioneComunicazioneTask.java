package it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione;

import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.operazioni.AggiungiAllegatoComunicazioneTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.operazioni.InviaComunicazioneTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.operazioni.RimuoviAllegatoComunicazioneTaskApi;

public interface GestioneComunicazioneTask extends Task<DatiGestioneComunicazioneTask>, AggiungiAllegatoComunicazioneTaskApi, RimuoviAllegatoComunicazioneTaskApi, InviaComunicazioneTaskApi {

}