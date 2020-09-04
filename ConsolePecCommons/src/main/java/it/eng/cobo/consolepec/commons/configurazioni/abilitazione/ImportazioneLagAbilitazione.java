package it.eng.cobo.consolepec.commons.configurazioni.abilitazione;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImportazioneLagAbilitazione implements Abilitazione {

	private static final long serialVersionUID = -8508585223976501479L;

	private Date dataCreazione;
	private String usernameCreazione;
}
