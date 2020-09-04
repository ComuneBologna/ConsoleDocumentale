package it.eng.consolepec.xmlplugin.tasks.gestionepec;

import it.eng.consolepec.xmlplugin.factory.DatiTask;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = true)
public class DatiGestionePECTask extends DatiTask {	

	protected DatiGestionePECTask(Assegnatario assegnatario) {
		setAssegnatario(assegnatario);		
	}
	
	
	@Override
	protected void setId(Integer id){
		super.setId(id);
	}
	
	@Override
	public void setAssegnatario(Assegnatario nuovo){
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
		Assegnatario assegnatario;

		@Setter Integer id;
		@Setter Boolean attivo;
		
		
		public Builder() {

		}

		public DatiGestionePECTask construct() {
			DatiGestionePECTask dati =  new DatiGestionePECTask(assegnatario);
			dati.setAttivo(attivo);
			dati.setId(id);
			return dati;
		}
	}
}