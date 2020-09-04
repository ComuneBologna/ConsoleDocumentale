package it.eng.consolepec.xmlplugin.pratica.template;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class CampoTemplate {
	@Getter
	@Setter
	String nome;
	@Getter
	@Setter
	TipoCampoTemplate tipo;
	@Getter
	@Setter
	String formato;
	@Getter
	@Setter
	String regexValidazione;
	@Getter
	@Setter
	Integer lunghezzaMassima;
	@Getter
	List<String> valoriLista;
	@Getter
	@Setter
	CampoMetadato campoMetadato;

	public enum TipoCampoTemplate {
		TEXT,
		TEXTAREA,
		DATE,
		LIST,
		INTEGER,
		DOUBLE,
		YESNO,
		METADATA;
	}
}
