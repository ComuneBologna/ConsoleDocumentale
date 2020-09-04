package it.eng.cobo.consolepec.commons.profilazione;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode(of = "username")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Utente implements Serializable {

	private static final long serialVersionUID = -3040388779691523870L;

	private String nome;
	private String cognome;
	private String nomeCompleto;
	private String username;
	private String matricola;
	private String dipartimento;
	private String codicefiscale;
	private String mail;
	private List<AnagraficaRuolo> anagraficheRuoli = new ArrayList<>();
	private AnagraficaRuolo ruoloPersonale;
	private boolean utenteEsterno;

	public List<String> getRuoli() {

		List<String> res = new ArrayList<String>();

		for (AnagraficaRuolo ar : anagraficheRuoli) {
			res.add(ar.getRuolo());
		}

		return res;
	}
}
