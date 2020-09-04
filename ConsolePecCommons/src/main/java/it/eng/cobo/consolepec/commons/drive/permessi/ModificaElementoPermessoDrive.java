package it.eng.cobo.consolepec.commons.drive.permessi;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ModificaElementoPermessoDrive implements PermessoDrive {

	private static final long serialVersionUID = 5124924471622136471L;

	private String ruolo;

	@Override
	public String getDescrizione() {
		return descrizione();
	}

	public static String descrizione() {
		return "Modifica";
	}

}
