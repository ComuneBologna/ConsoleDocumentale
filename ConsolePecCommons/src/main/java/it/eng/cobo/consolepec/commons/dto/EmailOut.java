package it.eng.cobo.consolepec.commons.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Giacomo F.M.
 * @since 2019-08-06
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class EmailOut extends Email {

	private static final long serialVersionUID = -7008180288796529782L;

	private Stato statoEmail;

	public static enum Stato {
		BOZZA, DA_INVIARE, NON_INVIATA, INATTESADIPRESAINCARICO, PRESAINCARICO, MANCATA_ACCETTAZIONE, MANCATA_CONSEGNA, MANCATA_CONSEGNA_IN_REINOLTRO, PREAVVISO_MANCATA_CONSEGNA,
		PARZIALMENTECONSEGNATA, CONSEGNATA, ARCHIVIATA, RICONSEGNATA, ELIMINATA, CONSEGNA_SENZA_ACCETTAZIONE;
	}
}
