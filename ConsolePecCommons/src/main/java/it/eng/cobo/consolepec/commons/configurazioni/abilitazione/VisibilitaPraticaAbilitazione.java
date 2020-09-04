package it.eng.cobo.consolepec.commons.configurazioni.abilitazione;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of="tipo")
public abstract class VisibilitaPraticaAbilitazione implements VisibilitaAbilitazione {

	private static final long serialVersionUID = -4067050609759315654L;
	
	@NonNull protected String tipo;
	
	protected VisibilitaPraticaAbilitazione(String tipo) {
		this.tipo = tipo;
	}
	
	protected Date dataCreazione;
	protected String usernameCreazione;
}
