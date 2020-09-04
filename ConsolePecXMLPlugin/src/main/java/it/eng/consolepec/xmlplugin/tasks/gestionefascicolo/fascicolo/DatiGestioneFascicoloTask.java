package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo;

import it.eng.consolepec.xmlplugin.factory.DatiTask;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class DatiGestioneFascicoloTask extends DatiTask {

	@Setter(value = AccessLevel.PRIVATE)
	@Getter
	private TreeSet<Condivisione> condivisioni = new TreeSet<DatiGestioneFascicoloTask.Condivisione>();

	public DatiGestioneFascicoloTask(Assegnatario assegnatario, List<Condivisione> condivisioni) {
		setAssegnatario(assegnatario);
		setCondivisioni(new TreeSet<DatiGestioneFascicoloTask.Condivisione>(condivisioni));
	}

	@Override
	public void setId(Integer id) {
		super.setId(id);
	}

	@Override
	public void setAssegnatario(Assegnatario nuovo) {
		super.setAssegnatario(nuovo);
	}

	@Override
	public Boolean getAttivo() {
		return super.getAttivo();
	}

	@Override
	public void setAttivo(Boolean attivo) {
		super.setAttivo(attivo);
	}

	@ToString
	@AllArgsConstructor
	@EqualsAndHashCode
	public static class Condivisione  implements Comparable<Condivisione>{
		@Getter
		private String nomeGruppo;
		@Getter
		private Date dataInizio;
		@Getter
		private TreeSet<DatiFascicolo.Operazione> operazioni = new TreeSet<DatiFascicolo.Operazione>();
		
		@Override
		public int compareTo(Condivisione o) {
			return nomeGruppo.compareTo(o.nomeGruppo);
		}
	}

	public static class Builder {
		@Setter
		Assegnatario assegnatario;
		@Setter
		List<Condivisione> condivisioni = new ArrayList<DatiGestioneFascicoloTask.Condivisione>();

		@Setter
		Integer id;
		@Setter
		Boolean attivo;

		public Builder() {

		}

		public DatiGestioneFascicoloTask construct() {
			DatiGestioneFascicoloTask dati = new DatiGestioneFascicoloTask(assegnatario, condivisioni);
			dati.setAttivo(attivo);
			dati.setId(id);
			return dati;
		}
	}
}
