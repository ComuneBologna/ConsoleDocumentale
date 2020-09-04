package it.eng.cobo.consolepec.commons.rubrica;

import java.util.Date;

import lombok.AccessLevel;
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
public final class PersonaFisica extends Anagrafica {

	private static final long serialVersionUID = -4790710455161783343L;

	@Setter(AccessLevel.NONE)
	private String codiceFiscale;
	private String nome;
	private String cognome;
	private String luogoNascita;
	private Date dataNascita;

	public void setCodiceFiscale(String codiceFiscale) {
		if (codiceFiscale != null) {
			this.codiceFiscale = codiceFiscale.toUpperCase();
		} else {
			this.codiceFiscale = codiceFiscale;
		}
	}

	@Override
	public void accept(AnagraficaVisitor persona) {
		persona.visit(this);
	}

}
