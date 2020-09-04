package it.eng.cobo.consolepec.commons.configurazioni.abilitazione;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of="tipo")
public abstract class ComunicazioneAbilitazione implements Abilitazione {

	private static final long serialVersionUID = 4059338234184383477L;

	@NonNull protected String tipo;
	
	protected ComunicazioneAbilitazione(String tipo) {
		this.tipo = tipo;
	}
	
	protected Date dataCreazione;
	protected String usernameCreazione;
}
