package it.eng.cobo.consolepec.commons.configurazioni.abilitazione;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true, of = {})
@NoArgsConstructor
public class VisibilitaPraticaModulisticaAbilitazione extends VisibilitaPraticaAbilitazione {
	private static final long serialVersionUID = 79081189126993784L;

	public VisibilitaPraticaModulisticaAbilitazione(String tipo) {
		super(tipo);
	}
}
