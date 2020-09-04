package it.eng.cobo.consolepec.commons.configurazioni.abilitazione;

import java.util.Date;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of="ruolo")
public class ModificaRuoloAbilitazione implements RuoloAbilitazione {
	private static final long serialVersionUID = -4718976461197533837L;
	
	private String ruolo;
	
	public ModificaRuoloAbilitazione(String ruolo) {
		this.ruolo = ruolo;
	}
	
	private Date dataCreazione;
	private String usernameCreazione;
}
