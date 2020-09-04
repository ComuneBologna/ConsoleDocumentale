package it.eng.cobo.consolepec.commons.urbanistica;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author AlessandroP
 * @since 23/nov/2017
 */
@Data
@NoArgsConstructor
public class AllegatoProcedi implements Serializable {

	private static final long serialVersionUID = 3314911147293772904L;

	private String numeroAnnoPG;

	private String nome;
	private String idAlfresco;
	private String tipologia;
	private boolean pubblico;

}
