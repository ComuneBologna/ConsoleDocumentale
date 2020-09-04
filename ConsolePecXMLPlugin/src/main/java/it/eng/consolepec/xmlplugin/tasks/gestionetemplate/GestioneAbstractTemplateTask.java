package it.eng.consolepec.xmlplugin.tasks.gestionetemplate;

import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.EliminaTemplateTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.GestionePresaInCaricoApiTask;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.ModificaTemplateTaskApi;

public interface GestioneAbstractTemplateTask<T extends DatiGestioneAbstractTemplateTask> extends Task<T>, EliminaTemplateTaskApi, GestionePresaInCaricoApiTask, ModificaTemplateTaskApi {

}
