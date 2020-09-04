package it.eng.cobo.consolepec.commons.datigenerici;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comune implements Serializable {

	private static final long serialVersionUID = -4000417372320771113L;

	private double codiceComune;
	private String codiceBelf;
	private String codiceIstat;
	private String descrizioneComune;
	private String provincia;
	private double cap;
	private Date dataCessazioneComune;
	private String descrizioneRegione;
	private Date dataIstituzioneComune;
	private double flagComuneCessato;
	private double codiceComuneAttuale;
	private double flagStradComune;

}
