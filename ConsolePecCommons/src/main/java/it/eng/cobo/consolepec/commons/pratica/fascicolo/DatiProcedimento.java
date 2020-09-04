package it.eng.cobo.consolepec.commons.pratica.fascicolo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author GiacomoFM
 * @since 16/gen/2018
 */
@NoArgsConstructor
@Data
public class DatiProcedimento implements Serializable {
	private static final long serialVersionUID = -2951999623765564916L;

	private String quartiere;
	private String email;
	private Date dataAvviamentoProcedimento; // Non utilizzato
	private int codiceProcedimento;

}
