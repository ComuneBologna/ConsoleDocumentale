package it.eng.consolepec.xmlplugin.tasks.gestionetemplate;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = true)
public class DatiGestioneTemplateEmailTask extends DatiGestioneAbstractTemplateTask {	

	protected DatiGestioneTemplateEmailTask(Assegnatario assegnatario) {
		super(assegnatario);		
	}
		
	public static class Builder extends DatiGestioneAbstractTemplateTask.Builder {
				
		public Builder() {
		}

		public DatiGestioneTemplateEmailTask construct() {
			DatiGestioneTemplateEmailTask dati =  new DatiGestioneTemplateEmailTask(assegnatario);
			dati.setAttivo(attivo);
			dati.setId(id);
			return dati;
		}
	}
	
}