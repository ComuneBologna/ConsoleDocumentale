package it.eng.cobo.consolepec.commons.drive.metadato;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Giacomo F.M.
 * @since 2019-05-28
 */
@Data
@EqualsAndHashCode(of = "chiave")
public abstract class Metadato implements Serializable {

	private static final long serialVersionUID = 6925532549925160495L;

	private String chiave;

	public abstract void accept(MetadatoVisitor v);

	public static interface MetadatoVisitor {

		void visit(MetadatoSingolo metadato);

		void visit(MetadatoMultiplo metadato);

	}
}
