package it.eng.cobo.consolepec.commons.pratica;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OperativitaRidotta implements Serializable {

	private static final long serialVersionUID = 4120117851965117973L;

	private List<OperazioneOperativitaRidotta> operazioni = new ArrayList<>();

	@Data
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class OperazioneOperativitaRidotta implements Serializable {

		private static final long serialVersionUID = -4253995596570206037L;

		private String nomeOperazione;
		private List<TipoAccesso> accessiConsentiti = new ArrayList<>();

		public OperazioneOperativitaRidotta(String nomeOperazione) {
			this.nomeOperazione = nomeOperazione;
		}

		public static enum TipoAccesso {
			ASSEGNATARIO, //
			SUPERVISORE, //
			COLLEGAMENTO, //
			UTENTE_ESTERNO, //
			MATR_VISIBILITA, //
			LETTURA; //

			public static TipoAccesso fromStringValue(String value) {
				for (TipoAccesso ta : TipoAccesso.values()) {
					if (ta.name().equals(value))
						return ta;
				}

				return null;
			}
		}
	}
}
