package it.eng.cobo.consolepec.commons.configurazioni;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false, of="ruolo")
@NoArgsConstructor
public class AnagraficaRuolo implements Configurabile, Serializable {

	private static final long serialVersionUID = -5787986989584483988L;
	
	private String ruolo;
	private String etichetta;
	private Stato stato;
	private String emailPredefinita;
	private List<String> operatori = new ArrayList<>();
	private boolean esterno;
	
	public static enum Stato {
		ATTIVA, DISATTIVA;
		
		public static List<String> labels() {
			List<String> labels = new ArrayList<String>();

			for (Stato stato : Stato.values()) {
				labels.add(stato.toString());
			}

			return labels;
		}
		
		public static Stato from(String value) {
			try {
				return Stato.valueOf(value);

			} catch (Exception e) {
				return null;
			}
		}
	}

	private List<Azione> azioni = new ArrayList<>();
	private Date dataCreazione;
	private String usernameCreazione;
}
