package it.eng.cobo.consolepec.commons.configurazioni.abilitazione;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true, of = {})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreazioneFascicoloAbilitazione extends FascicoloAbilitazione implements CreazionePraticaAbilitazione {
	private static final long serialVersionUID = -138523560419551321L;

	public CreazioneFascicoloAbilitazione(String tipo) {
		super(tipo);
	}
}
