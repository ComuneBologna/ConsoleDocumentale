package it.eng.consolepec.xmlplugin.tasks.gestionetemplate;

import it.eng.consolepec.xmlplugin.factory.DatiTask;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = true)
public class DatiGestioneAbstractTemplateTask extends DatiTask{
	
	protected DatiGestioneAbstractTemplateTask(Assegnatario assegnatario) {
		setAssegnatario(assegnatario);		
	}
	
	@Override
	protected void setId(Integer id){
		super.setId(id);
	}
	
	@Override
	protected void setAssegnatario(Assegnatario nuovo){
		super.setAssegnatario(nuovo);
	}
	
	@Override
	protected Boolean getAttivo() {
		return super.getAttivo();
	}
	
	@Override
	protected void setAttivo(Boolean attivo) {
		super.setAttivo(attivo);
	}
	
	public static class Builder {
		@Setter
		protected Assegnatario assegnatario;
		
		@Setter
		protected Integer id;
		@Setter
		protected Boolean attivo;
		
		public Builder() {
		}

		public DatiGestioneAbstractTemplateTask construct() {
			DatiGestioneAbstractTemplateTask dati =  new DatiGestioneAbstractTemplateTask(assegnatario);
			dati.setAttivo(attivo);
			dati.setId(id);
			return dati;
		}
	}

}
