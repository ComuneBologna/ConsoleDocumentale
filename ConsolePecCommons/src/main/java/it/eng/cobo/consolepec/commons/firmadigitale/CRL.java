package it.eng.cobo.consolepec.commons.firmadigitale;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author GiacomoFM
 * @since 16/feb/2018
 */
@NoArgsConstructor
@AllArgsConstructor
public class CRL implements Serializable {

	private static final long serialVersionUID = -8454592805410935005L;

	@Getter private String value;
	@Getter private Stato stato;
	@Getter private boolean revocato;

	public static enum Stato {
		OK("valida"), OFFLINE("non verificata");

		@Getter private String value;

		private Stato(final String value) {
			this.value = value;
		}

		public static Stato getEnum(String str) {
			for (Stato s : values()) {
				if (s.toString().equalsIgnoreCase(str)) {
					return s;
				}
			}
			return OFFLINE;
		}
	}

}
