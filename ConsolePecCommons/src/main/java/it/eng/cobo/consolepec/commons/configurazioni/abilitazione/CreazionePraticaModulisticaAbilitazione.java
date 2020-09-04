package it.eng.cobo.consolepec.commons.configurazioni.abilitazione;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true, of = {})
@NoArgsConstructor
public class CreazionePraticaModulisticaAbilitazione extends PraticaModulisticaAbilitazione implements CreazionePraticaAbilitazione {
	private static final long serialVersionUID = 1720238831143447856L;

	public CreazionePraticaModulisticaAbilitazione(String tipo) {
		super(tipo);
	}
}
