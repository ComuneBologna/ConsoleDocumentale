package it.eng.cobo.consolepec.commons.configurazioni.abilitazione;

import java.util.Date;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of={"nome", "posizione"})
public class WorklistAbilitazione implements Abilitazione {

	private static final long serialVersionUID = 519992471093311664L;
	
	@NonNull private String nome;
	private int posizione;
	
	public WorklistAbilitazione(String nome, int posizione) {
		this.nome = nome;
		this.posizione = posizione;
	}
	
	private Date dataCreazione;
	private String usernameCreazione;
}
