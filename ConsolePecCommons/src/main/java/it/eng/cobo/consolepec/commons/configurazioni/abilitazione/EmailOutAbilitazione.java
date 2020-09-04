package it.eng.cobo.consolepec.commons.configurazioni.abilitazione;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of={"tipo", "indirizzo"})
public abstract class EmailOutAbilitazione implements Abilitazione {

	private static final long serialVersionUID = 1770405974289094828L;

	@NonNull private String tipo;
	@NonNull private String indirizzo;
	
	protected EmailOutAbilitazione(String tipo, String indirizzo) {
		this.tipo = tipo;
		this.indirizzo = indirizzo;
	}
	
	protected Date dataCreazione;
	protected String usernameCreazione;
}
