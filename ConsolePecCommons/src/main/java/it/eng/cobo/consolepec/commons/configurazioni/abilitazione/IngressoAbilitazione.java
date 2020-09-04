package it.eng.cobo.consolepec.commons.configurazioni.abilitazione;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = { "tipo", "indirizzo" })
public abstract class IngressoAbilitazione implements Abilitazione {

	private static final long serialVersionUID = 461619066785930352L;

	@NonNull protected String tipo;
	@NonNull protected String indirizzo;

	protected IngressoAbilitazione(String tipo, String indirizzo) {
		this.tipo = tipo;
		this.indirizzo = indirizzo;
	}

	protected Date dataCreazione;
	protected String usernameCreazione;
}
