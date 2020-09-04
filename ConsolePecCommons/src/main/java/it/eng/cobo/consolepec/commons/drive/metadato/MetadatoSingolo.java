package it.eng.cobo.consolepec.commons.drive.metadato;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Giacomo F.M.
 * @since 2019-05-28
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, of = "valore")
public class MetadatoSingolo extends Metadato {

	private static final long serialVersionUID = 1102741130110714724L;

	private String valore;

	public MetadatoSingolo(final String chiave) {
		super();
		setChiave(chiave);
	}

	@Override
	public String toString() {
		return "(chiave=" + getChiave() + ",valore=" + getValore() + ")";
	}

	@Override
	public void accept(MetadatoVisitor v) {
		v.visit(this);
	}

}
