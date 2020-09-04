package it.eng.cobo.consolepec.commons.configurazioni.abilitazione;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of="tipo")
public abstract class PraticaModulisticaAbilitazione implements Abilitazione {
	private static final long serialVersionUID = -2908721519243448981L;

	@NonNull protected String tipo;
	
	protected PraticaModulisticaAbilitazione(String tipo) {
		this.tipo = tipo;
	}
	
	protected Date dataCreazione;
	protected String usernameCreazione;
}
