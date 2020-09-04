package it.eng.consolepec.xmlplugin.tasks.gestionemodulistica;

import it.eng.consolepec.xmlplugin.factory.DatiTask;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = true)
public class DatiGestionePraticaModulisticaTask extends DatiTask {	

	protected DatiGestionePraticaModulisticaTask(Assegnatario assegnatario) {
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
		Assegnatario assegnatario;
		
		@Setter
		Integer id;
		@Setter
		Boolean attivo;
		
		public Builder() {
		}

		public DatiGestionePraticaModulisticaTask construct() {
			DatiGestionePraticaModulisticaTask dati =  new DatiGestionePraticaModulisticaTask(assegnatario);
			dati.setAttivo(attivo);
			dati.setId(id);
			return dati;
		}
	}
	
}