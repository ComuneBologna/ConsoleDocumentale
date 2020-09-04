package it.eng.cobo.consolepec.commons.drive;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Giacomo F.M.
 * @since 2019-05-14
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class File extends DriveElement {

	private static final long serialVersionUID = -3072523192061182320L;

	public static final String PRE_ID = "FI";

	private String idCartella;

	private String idAlfresco;
	private String versione;

	private String mimetype;

	public File() {
		setCartella(false);
	}

}
