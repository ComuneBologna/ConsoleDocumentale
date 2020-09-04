package it.eng.consolepec.xmlplugin.tasks.gestionetemplate;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = true)
public class DatiGestioneTemplateDocumentoPDFTask extends DatiGestioneAbstractTemplateTask {	

	protected DatiGestioneTemplateDocumentoPDFTask(Assegnatario assegnatario) {
		super(assegnatario);		
	}
		
	public static class Builder extends DatiGestioneAbstractTemplateTask.Builder {
		
		
		public Builder() {
		}

		public DatiGestioneTemplateDocumentoPDFTask construct() {
			DatiGestioneTemplateDocumentoPDFTask dati =  new DatiGestioneTemplateDocumentoPDFTask(assegnatario);
			dati.setAttivo(attivo);
			dati.setId(id);
			return dati;
		}
	}
	
}