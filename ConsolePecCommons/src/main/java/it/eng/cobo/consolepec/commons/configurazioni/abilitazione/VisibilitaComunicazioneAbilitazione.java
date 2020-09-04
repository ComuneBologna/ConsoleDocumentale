package it.eng.cobo.consolepec.commons.configurazioni.abilitazione;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true, of = {})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VisibilitaComunicazioneAbilitazione extends VisibilitaPraticaAbilitazione {
	private static final long serialVersionUID = -3836522783240652853L;

	public VisibilitaComunicazioneAbilitazione(String tipo) {
		super(tipo);
	}

}
