package it.eng.cobo.consolepec.commons.configurazioni.abilitazione;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true, of = {})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreazioneComunicazioneAbilitazione extends ComunicazioneAbilitazione implements CreazionePraticaAbilitazione {
	private static final long serialVersionUID = -1357097270140285156L;

	public CreazioneComunicazioneAbilitazione(String tipo) {
		super(tipo);
	}
}
