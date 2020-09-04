package it.eng.consolepec.xmlplugin.tasks.riattiva;

import it.eng.consolepec.xmlplugin.factory.DatiTask;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = true)
public class DatiRiattivazioneTask extends DatiTask {
	
	@Getter @Setter(value=AccessLevel.PROTECTED) Integer idTaskDaRiattivare;

	public DatiRiattivazioneTask(Assegnatario assegnatario) {
		setAssegnatario(assegnatario);
	}

	@Override
	protected void setId(Integer id) {
		super.setId(id);
	}

	@Override
	protected void setAssegnatario(Assegnatario nuovo) {
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
		Integer idTaskDaRiattivare;
		@Setter
		Boolean attivo;

		public Builder() {

		}

		public DatiRiattivazioneTask construct() {
			DatiRiattivazioneTask dati = new DatiRiattivazioneTask(assegnatario);
			dati.setAttivo(attivo);
			dati.setId(id);
			dati.setIdTaskDaRiattivare(idTaskDaRiattivare);
			return dati;
		}
	}

}
