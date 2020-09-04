package it.eng.consolepec.xmlplugin.tasks.gestionetemplate;

import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.AggiungiAllegatoTemplateTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.RimuoviAllegatoTemplateTaskApi;


public interface GestioneTemplateEmailTask extends GestioneAbstractTemplateTask<DatiGestioneTemplateEmailTask>, AggiungiAllegatoTemplateTaskApi, RimuoviAllegatoTemplateTaskApi {

}