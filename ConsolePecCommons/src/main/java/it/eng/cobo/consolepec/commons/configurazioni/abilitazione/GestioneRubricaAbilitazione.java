package it.eng.cobo.consolepec.commons.configurazioni.abilitazione;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GestioneRubricaAbilitazione implements Abilitazione {
	private static final long serialVersionUID = 9095031060494456192L;

	private Date dataCreazione;
	private String usernameCreazione;
}
