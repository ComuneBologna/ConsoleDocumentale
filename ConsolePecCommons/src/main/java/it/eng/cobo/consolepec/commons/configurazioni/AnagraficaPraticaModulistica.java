package it.eng.cobo.consolepec.commons.configurazioni;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true, of = {})
@NoArgsConstructor
public class AnagraficaPraticaModulistica extends TipologiaPratica implements Configurabile {
	private static final long serialVersionUID = -5226128252946372528L;

	private String nomeModulo;
	private List<CampoModulo> campiModulo;

	@Data
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor
	public static class CampoModulo implements Serializable {
		private static final long serialVersionUID = 349140661967289779L;

		private String nome;
		private String valore;
	}

	private List<Azione> azioni = new ArrayList<>();
	private Date dataCreazione;
	private String usernameCreazione;
}
