package it.eng.cobo.consolepec.commons.configurazioni.abilitazione;

import java.util.Date;

import lombok.Data;

@Data
public abstract class AmministrazioneAbilitazione implements Abilitazione {

	private static final long serialVersionUID = 6845994214717159180L;
	
	protected Date dataCreazione;
	protected String usernameCreazione;
}
