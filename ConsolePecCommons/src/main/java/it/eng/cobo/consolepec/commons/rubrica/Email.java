package it.eng.cobo.consolepec.commons.rubrica;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author GiacomoFM
 * @since 19/ott/2017
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Email implements Serializable {

	private static final long serialVersionUID = -3183511025236463615L;

	private String tipologia;
	private String email;

	@Override
	public String toString() {
		return tipologia + ": " + email;
	}

}
