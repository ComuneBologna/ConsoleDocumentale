package it.eng.cobo.consolepec.commons.configurazioni.abilitazione;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true, of = {})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreazioneEmailOutAbilitazione extends EmailOutAbilitazione implements CreazionePraticaAbilitazione {
	private static final long serialVersionUID = -3581485601561438670L;

	public CreazioneEmailOutAbilitazione(String tipo, String indirizzo) {
		super(tipo, indirizzo);
	}
}
