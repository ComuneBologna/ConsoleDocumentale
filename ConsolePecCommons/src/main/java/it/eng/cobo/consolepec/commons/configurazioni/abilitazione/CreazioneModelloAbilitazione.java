package it.eng.cobo.consolepec.commons.configurazioni.abilitazione;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true, of = {})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreazioneModelloAbilitazione extends ModelloAbilitazione implements CreazionePraticaAbilitazione {
	private static final long serialVersionUID = -7339466255665518693L;

	public CreazioneModelloAbilitazione(String tipo) {
		super(tipo);
	}
}
