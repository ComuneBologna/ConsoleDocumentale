package it.eng.consolepec.xmlplugin.factory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class DatiTask {
	@Getter
	@Setter(value = AccessLevel.PROTECTED)
	Integer id;
	@Getter(value = AccessLevel.PUBLIC)
	List<Assegnatario> assegnatariPassati = new ArrayList<DatiTask.Assegnatario>();
	@Getter
	@Setter(value = AccessLevel.PROTECTED)
	Assegnatario assegnatario;
	@Getter(value = AccessLevel.PROTECTED)
	@Setter(value = AccessLevel.PROTECTED)
	Boolean attivo;
	@Getter(value = AccessLevel.PROTECTED)
	@Setter(value = AccessLevel.PROTECTED)
	String stato;

	@AllArgsConstructor
	@ToString
	@EqualsAndHashCode
	public static class Assegnatario {
		@Getter
		String nome;
		@Getter
		@Setter
		String etichetta;
		@Getter
		Date dataInizio;
		@Getter
		@Setter
		Date dataFine;

	}

	public enum TipoTask {
		GestioneFascicoloTask, GestionePECTask, RiportaInLavorazioneTask, GestionePraticaModulisticaTask, GestioneTemplateTask, GestioneComunicazioneTask, RichiestaFirmaTask;
	}
}
