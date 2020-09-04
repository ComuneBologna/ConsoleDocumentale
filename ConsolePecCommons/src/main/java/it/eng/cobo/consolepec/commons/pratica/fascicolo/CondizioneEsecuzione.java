package it.eng.cobo.consolepec.commons.pratica.fascicolo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author biagiot
 *
 */
public interface CondizioneEsecuzione extends Serializable {

	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@Data
	public static class CambiaStepIterCondizioneEsecuzione implements CondizioneEsecuzione {

		private static final long serialVersionUID = -6326557582578933580L;

		public String valore;
	}

	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@Data
	public abstract static class ModificaDatoAggiuntivoCondizioneEsecuzione implements CondizioneEsecuzione {

		private static final long serialVersionUID = -7196477547081136997L;

		private String nome;
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class ModificaDatoAggiuntivoValoreCondizioneEsecuzione extends ModificaDatoAggiuntivoCondizioneEsecuzione {

		private static final long serialVersionUID = 310354666756487513L;

		private String valore;

		@Builder
		public ModificaDatoAggiuntivoValoreCondizioneEsecuzione(String nome, String valore) {
			super(nome);
			this.valore = valore;
		}
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class ModificaDatoAggiuntivoValoriCondizioneEsecuzione extends ModificaDatoAggiuntivoCondizioneEsecuzione {

		private static final long serialVersionUID = -3800596783064863426L;

		private List<String> valori = new ArrayList<String>();

		@Builder
		public ModificaDatoAggiuntivoValoriCondizioneEsecuzione(String nome, List<String> valori) {
			super(nome);
			this.valori = valori;
		}
	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class RibaltaDatiAggiuntiviDaAnagraficaCondizioneEsecuzione implements CondizioneEsecuzione {
		private static final long serialVersionUID = -5062752640927934938L;
	}
}
