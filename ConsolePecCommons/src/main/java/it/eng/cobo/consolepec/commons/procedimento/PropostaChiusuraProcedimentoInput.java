package it.eng.cobo.consolepec.commons.procedimento;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
public class PropostaChiusuraProcedimentoInput implements Serializable {

	private static final long serialVersionUID = -5747630279015544202L;

	private String annoProtocollazione;
	private String numProtocollazione;
	private TipoEstrazione tipoEstrazione;

	@AllArgsConstructor
	public static enum TipoEstrazione {
		T("Tutti"), C("Procedimenti chiusi"), A("Procedimenti aperti");

		@Getter
		private String descrizione;
	}
}
