package it.eng.cobo.consolepec.commons.rubrica;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author GiacomoFM
 * @since 14/set/2017
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class Indirizzo implements Serializable {

	private static final long serialVersionUID = 5488988604991469031L;

	public static final String TIPOLOGIA_INDIRIZZO_LAG = "RESIDENZA";

	private String via;
	private String civico;
	private String esponente;
	private String interno;
	private String piano;
	private String tipologia;
	private String comune;
	private String cap;
	private String nazione;

	@Override
	public String toString() {
		return toString(true);
	}

	public String toString(boolean tipologia) {
		StringBuilder sb = new StringBuilder();
		if (tipologia && this.tipologia != null && !this.tipologia.isEmpty()) {
			sb.append(this.tipologia).append(": ");
		}
		sb.append(via);
		if (civico != null && !civico.isEmpty()) {
			sb.append(", ").append(civico);
			if (esponente != null && !esponente.isEmpty())
				sb.append("/").append(esponente);
			if (interno != null && !interno.isEmpty())
				sb.append(", interno n.").append(interno);
			if (piano != null && !piano.isEmpty())
				sb.append(", piano n.").append(piano);
		}
		if (comune != null && !comune.isEmpty())
			sb.append(", ").append(comune);
		if (cap != null && !cap.isEmpty())
			sb.append(", ").append(cap);
		if (nazione != null && !nazione.isEmpty())
			sb.append(", ").append(nazione);
		return sb.toString();
	}
}
