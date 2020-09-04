package it.eng.cobo.consolepec.commons.configurazioni.abilitazione;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of="tipo")
public abstract class ModelloAbilitazione implements Abilitazione {

	private static final long serialVersionUID = -5296236101378057491L;

	@NonNull private String tipo;
	
	protected ModelloAbilitazione(String tipo) {
		this.tipo = tipo;
	}
	
	protected Date dataCreazione;
	protected String usernameCreazione;
}
