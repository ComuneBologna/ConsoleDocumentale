package it.eng.cobo.consolepec.commons.drive.permessi;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class VisualizzaElementoPermessoDrive implements PermessoDrive {

	private static final long serialVersionUID = 8877689980241552338L;

	private String ruolo;

	@Override
	public String getDescrizione() {
		return descrizione();
	}

	public static String descrizione() {
		return "Visualizzazione";
	}

}
