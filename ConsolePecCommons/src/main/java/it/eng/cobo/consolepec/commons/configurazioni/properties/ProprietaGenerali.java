package it.eng.cobo.consolepec.commons.configurazioni.properties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProprietaGenerali implements Serializable {

	private static final long serialVersionUID = -4204501782534425040L;

	private Integer maxRisultatiWorklist;
	private Integer intervalloAggiornamentoWorklist;
	private List<Server> serverIngressiAbilitati = new ArrayList<>();

	@Getter
	public static class Server implements Serializable {

		private static final long serialVersionUID = -340515715227901563L;

		String nome;
		TipoMail tipoMail;

		public static enum TipoMail {
			PEC, MAIL, FAX;
		}
	}
}
