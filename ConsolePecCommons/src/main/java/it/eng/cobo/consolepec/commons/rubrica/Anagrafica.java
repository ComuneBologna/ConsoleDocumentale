package it.eng.cobo.consolepec.commons.rubrica;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.eng.cobo.consolepec.commons.profilazione.Azione;
import lombok.Getter;
import lombok.Setter;

/**
 * @author GiacomoFM
 * @since 18/set/2017
 */
@Getter
public abstract class Anagrafica implements Serializable {

	private static final long serialVersionUID = 2742111305711080622L;

	@Setter
	private double id;

	@Setter
	private Stato stato;

	public static enum Stato {
		ATTIVA, DISATTIVA
	}

	private List<Indirizzo> indirizzi = new ArrayList<>();
	private List<Telefono> telefoni = new ArrayList<>();
	private List<Email> email = new ArrayList<>();

	private List<Azione> azioni = new ArrayList<>();

	private List<String> gruppi = new ArrayList<>();

	public String elencoGruppi() {
		StringBuilder sb = new StringBuilder();
		for (String str : gruppi) {
			sb.append(", ").append(str);
		}
		return sb.toString().length() >= 2 ? sb.substring(2) : "Nessun gruppo indicato";
	}

	public abstract void accept(AnagraficaVisitor ag);

	public interface AnagraficaVisitor {
		void visit(PersonaFisica personaFisica);

		void visit(PersonaGiuridica personaGiuridica);
	}

}
