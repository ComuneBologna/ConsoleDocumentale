/**
 * 
 */
package it.eng.cobo.consolepec.commons.configurazioni;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author AlessandroP
 * @since 13/feb/2018
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Azione implements Serializable {

	private static final long serialVersionUID = -8102118160184450755L;

	private String usernameUtente;
	private Date data;
	private String descrizione;
}
