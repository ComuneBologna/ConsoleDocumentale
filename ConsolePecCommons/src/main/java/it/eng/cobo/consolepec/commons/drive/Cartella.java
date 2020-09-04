package it.eng.cobo.consolepec.commons.drive;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Giacomo F.M.
 * @since 2019-05-14
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Cartella extends DriveElement {

	private static final long serialVersionUID = -2068978657011294342L;

	public static final String SEPARATORE = "/";
	public static final String PRE_ID = "CA";

	private String idPadre;

	@EqualsAndHashCode.Exclude
	private String nomenclatura;

	@Setter(AccessLevel.NONE)
	@EqualsAndHashCode.Exclude
	private List<String> idFigli = new ArrayList<>();

	public Cartella() {
		setCartella(true);
	}

}
