package it.eng.cobo.consolepec.commons.procedimento;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.Getter;

@Data
public class ChiusuraProcedimentoInput implements Serializable {

	private static final long serialVersionUID = 2963442486162316936L;

	private String codUtente;
	private int annoProtocollazione;
	private String numProtocollazione;
	private Integer codTipologiaProcedimento;
	private ModalitaChiusuraProcedimento modalitaChiusura;
	private Date dataChiusura;
	private String numProtocolloDocChiusura;
	private Integer annoProtocolloDocChiusura;
	private Integer codiceEventoChiusura;

	public static enum ModalitaChiusuraProcedimento {
		R("PER RINUNCIA"), U("DI UFFICIO"), G("NORMALE");

		@Getter
		String descrizione;

		ModalitaChiusuraProcedimento(String descrizione) {
			this.descrizione = descrizione;
		}

		public static ModalitaChiusuraProcedimento fromDescrizione(String descrizione) {
			for (ModalitaChiusuraProcedimento m : ModalitaChiusuraProcedimento.values()) {
				if (m.getDescrizione().equals(descrizione)) {
					return m;
				}
			}

			return null;
		}
	}
}
