package it.eng.cobo.consolepec.commons.drive.metadato;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Giacomo F.M.
 * @since 2019-05-28
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, of = "valori")
public class MetadatoMultiplo extends Metadato {

	private static final long serialVersionUID = 4183467543663507453L;

	@Setter(AccessLevel.NONE)
	private List<String> valori = new ArrayList<>();

	public MetadatoMultiplo(final String chiave) {
		super();
		setChiave(chiave);
	}

	@Override
	public String toString() {
		return "(chiave=" + getChiave() + ",valori=" + getValori() + ")";
	}

	@Override
	public void accept(MetadatoVisitor v) {
		v.visit(this);
	}

}
