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
public class Telefono implements Serializable {

	private static final long serialVersionUID = -8424342843211193500L;

	private String tipologia;
	private String numero;

	@Override
	public String toString() {
		return tipologia + ": " + numero;
	}

}
