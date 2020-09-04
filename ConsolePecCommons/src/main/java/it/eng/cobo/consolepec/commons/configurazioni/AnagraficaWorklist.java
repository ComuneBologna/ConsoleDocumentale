package it.eng.cobo.consolepec.commons.configurazioni;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AnagraficaWorklist implements Configurabile, Serializable {

	private static final long serialVersionUID = 1505406778103123382L;

	private String nome;
	private String titoloWorklist;
	private String titoloMenu;
	private String nameTokenWorklist;
	private String nameTokenDettaglio;
	private Stato stato;
	private int maxRisultati;
	private Map<String, Object> parametriFissiWorklist = new HashMap<String, Object>();
	private Tipo tipo;

	public static enum Stato {
		ATTIVA, DISATTIVA;
	}

	public static enum Tipo {
		WORKLIST_PRATICA, WORKLIST_ATTIVITA;
	}

	private Date dataCreazione;
	private List<Azione> azioni = new ArrayList<Azione>();
	private String usernameCreazione;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Counter implements Serializable {
		private static final long serialVersionUID = 3324439384635671624L;

		int daLeggere;
		int totale;
	}

	/**
	 * XXX: Serve in creazione di un nuovo ruolo, per abilitarlo di default alla cartella di firma. Va eliminato e gestito diversamente e in modo configurabile
	 *
	 * @author biagiot
	 *
	 */
	public static enum TipoWorklistAttivita {
		WORKLIST_CARTELLA_FIRMA;
	}

}
