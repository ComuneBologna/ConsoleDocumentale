package it.eng.cobo.consolepec.commons.pratica.fascicolo;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author GiacomoFM
 * @since 16/gen/2018
 */
@NoArgsConstructor
@Data
public class Titolazione implements Serializable {
	private static final long serialVersionUID = 9189094950490207046L;

	private String tipologiaDocumento;
	private String titolo;
	private String rubrica;
	private String sezione;

}
