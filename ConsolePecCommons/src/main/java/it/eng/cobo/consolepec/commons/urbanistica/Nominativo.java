package it.eng.cobo.consolepec.commons.urbanistica;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author GiacomoFM
 * @since 30/nov/2017
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Nominativo implements Serializable {

	private static final long serialVersionUID = -7551440585528803118L;

	private String tipoTitolo;
	private String cognomeNome;

}
