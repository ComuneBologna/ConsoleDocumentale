package it.eng.consolepec.spagicclient.remoteproxy.result;

import java.util.Date;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class CapofilaDetail {
	@Getter
	@Setter
	private String numeroPg, numeroPgCapofila, oggetto, numeroFascicolo;
	@Getter
	@Setter
	private int annoPg, annoPgCapofila, titolo, rubrica, sezione, annoFascicolo;
	@Getter
	@Setter
	private Date dataProtocollazione;
	@Getter
	@Setter
	private boolean capofila, completo;
}
