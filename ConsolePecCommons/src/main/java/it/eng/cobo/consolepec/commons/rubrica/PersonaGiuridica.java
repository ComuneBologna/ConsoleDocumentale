package it.eng.cobo.consolepec.commons.rubrica;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author GiacomoFM
 * @since 18/set/2017
 */
@Getter
@Setter
@NoArgsConstructor
public final class PersonaGiuridica extends Anagrafica {

	private static final long serialVersionUID = -9151960289188561533L;

	private String partitaIva;
	private String ragioneSociale;

	@Override
	public void accept(AnagraficaVisitor persona) {
		persona.visit(this);
	}

}
