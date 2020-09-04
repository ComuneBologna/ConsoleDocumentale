package it.eng.cobo.consolepec.commons.profilazione;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author GiacomoFM
 * @since 02/nov/2017
 */
@Getter
@NoArgsConstructor
public class Azione implements Serializable {

	private static final long serialVersionUID = 207026438045166958L;

	private Date data;
	@Setter
	private String azione;

	public Azione(String azione) {
		this.data = new Date();
		this.azione = azione;
	}

}
