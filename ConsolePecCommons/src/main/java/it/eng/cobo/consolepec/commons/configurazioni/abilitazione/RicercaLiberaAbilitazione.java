package it.eng.cobo.consolepec.commons.configurazioni.abilitazione;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RicercaLiberaAbilitazione implements Abilitazione {

	private static final long serialVersionUID = -2511190585074627865L;
	
	private Date dataCreazione;
	private String usernameCreazione;
}
