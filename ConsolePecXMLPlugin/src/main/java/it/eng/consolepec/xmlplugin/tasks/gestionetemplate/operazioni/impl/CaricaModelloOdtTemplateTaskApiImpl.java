package it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.impl;

import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.cobo.consolepec.commons.exception.InvalidArgumentException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.TaskObserver.AggiungiAllegato;
import it.eng.consolepec.xmlplugin.pratica.template.DatiAbstractTemplate.Stato;
import it.eng.consolepec.xmlplugin.pratica.template.TemplateDocumentoPDF;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.DatiGestioneTemplateDocumentoPDFTask;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.XMLGestioneAbstractTemplateTask;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.CaricaModelloOdtTemplateTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.TipoApiTaskTemplate;

public class CaricaModelloOdtTemplateTaskApiImpl extends AbstractTemplateTaskApiImpl<DatiGestioneTemplateDocumentoPDFTask> implements CaricaModelloOdtTemplateTaskApi {

	public CaricaModelloOdtTemplateTaskApiImpl(XMLGestioneAbstractTemplateTask<DatiGestioneTemplateDocumentoPDFTask> task) {
		super(task);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}

	@Override
	protected TipoApiTaskTemplate getTipoApiTask() {
		return TipoApiTaskTemplate.CARICA_ODT;
	}

	@Override
	public void caricaODT(Allegato doc, AggiungiAllegato handler) throws ApplicationException, InvalidArgumentException {
		String nomeAllegato = doc.getNome();
		Allegato aggiungiDocumentoODT = handler.aggiungiAllegato(
				new StringBuilder().append(getDatiTemplate().getFolderPath()).append("/").append(task.getEnclosingPratica().getSubFolderPath()).toString(), nomeAllegato);
		TemplateDocumentoPDF tpl = (TemplateDocumentoPDF) task.getEnclosingPratica();
		tpl.getDati().getAllegati().clear(); // XXX : Questo perchè deve esserci solo un allegato (odt) nel template pdf
		tpl.getDati().getAllegati().add(aggiungiDocumentoODT);
		tpl.getDati().setStato(Stato.IN_GESTIONE);
		generaEvento(EventiIterTemplate.CARICA_ALLEGATO, task.getCurrentUser(), aggiungiDocumentoODT.getLabel());
	}
}
