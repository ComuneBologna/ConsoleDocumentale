package it.eng.cobo.consolepec.commons.firmadigitale;

import java.io.Serializable;
import java.util.Date;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author GiacomoFM
 * @since 16/feb/2018
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Firmatario implements Serializable {

	private static final long serialVersionUID = -5574084908044974300L;

	@Getter private String DN;
	@Getter private String CA;
	@Getter private Date validoDal;
	@Getter private Date validoAl;
	@Getter private Date dataFirma;
	@Getter private Stato stato;
	@Getter private String descrizione;
	@Getter private TipoFirma tipoFirma;
	@Getter private CRL CRL;

	public static enum Stato {
		OK, KO, NF;

		public static Stato getEnum(String str) {
			for (Stato s : values()) {
				if (s.toString().equalsIgnoreCase(str)) {
					return s;
				}
			}
			return NF;
		}
	}

	public static enum TipoFirma {
		PADES, CADES, XADES, ND;

		public static TipoFirma getEnum(String str) {
			for (TipoFirma t : values()) {
				if (t.toString().equalsIgnoreCase(str)) {
					return t;
				}
			}
			return ND;
		}
	}

}
