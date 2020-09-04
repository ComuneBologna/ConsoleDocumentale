package it.eng.cobo.consolepec.commons.configurazioni.abilitazione;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class GestioneDriveAbilitazione implements Abilitazione {
	private static final long serialVersionUID = 8405203595009046257L;

	private Date dataCreazione;
	private String usernameCreazione;
}
