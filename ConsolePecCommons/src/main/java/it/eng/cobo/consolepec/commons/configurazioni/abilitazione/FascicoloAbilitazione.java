package it.eng.cobo.consolepec.commons.configurazioni.abilitazione;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of="tipo")
public abstract class FascicoloAbilitazione implements Abilitazione {

	private static final long serialVersionUID = 7049351167868670891L;

	@NonNull protected String tipo;
	
	protected FascicoloAbilitazione(String tipo) {
		this.tipo = tipo;
	}
	
	protected Date dataCreazione;
	protected String usernameCreazione;
}
