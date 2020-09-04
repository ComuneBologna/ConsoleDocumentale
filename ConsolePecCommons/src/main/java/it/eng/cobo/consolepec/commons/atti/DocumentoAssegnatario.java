package it.eng.cobo.consolepec.commons.atti;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author GiacomoFM
 * @since 09/mar/2018
 */
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoAssegnatario {

	@Getter private String nome;
	@Getter private String etichetta;
	@Getter private Date dataInizio;
	@Getter private Date dataFine;

}
