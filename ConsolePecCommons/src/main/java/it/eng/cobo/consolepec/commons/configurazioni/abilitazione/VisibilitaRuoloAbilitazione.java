package it.eng.cobo.consolepec.commons.configurazioni.abilitazione;

import java.util.Date;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of="ruolo")
public class VisibilitaRuoloAbilitazione implements VisibilitaAbilitazione, RuoloAbilitazione {
	private static final long serialVersionUID = -8717875528984361060L;

	private String ruolo;
	
	public VisibilitaRuoloAbilitazione(String ruolo) {
		this.ruolo = ruolo;
	}
	
	private Date dataCreazione;
	private String usernameCreazione;
}
