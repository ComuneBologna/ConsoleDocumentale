package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import it.eng.consolepec.xmlplugin.util.IdentificativoProtocollazione;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public interface AggiornaPGTaskApi extends ITaskApi {

	boolean aggiornamentoValido(List<IdentificativoProtocollazione> protocollazioni);

	void aggiornaPG(AggiornaPGTaskApiDati datiDiAggiornamento);

	// bean di supporto
	@Getter
	@Setter
	public static class AggiornaPGTaskApiDati {
		protected IdentificativoProtocollazione identificativo;

		protected String oggetto;
		protected String provenienza;
		protected String titolo;
		protected String rubrica;
		protected String sezione;
		protected Date dataProtocollazione;

		public AggiornaPGTaskApiDati(IdentificativoProtocollazione identificativo) {
			super();
			this.identificativo = identificativo;
		}

	}
}
