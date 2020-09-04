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
public class EmailIn extends Email {

	private static final long serialVersionUID = 8030696851629255881L;

	private Stato statoEmail;

	public static enum Stato {
		IN_GESTIONE, ARCHIVIATA, NOTIFICATA, ELIMINATA, RESPINTA, RICONSEGNATA, SCARTATA
	}

}
