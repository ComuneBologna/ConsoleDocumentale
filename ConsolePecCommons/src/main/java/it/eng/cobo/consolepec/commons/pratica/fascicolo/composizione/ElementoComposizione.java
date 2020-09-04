package it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione;

import java.io.Serializable;
import java.util.Date;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author GiacomoFM
 * @since 15/gen/2019
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ElementoComposizione implements Serializable {

	private static final long serialVersionUID = 8051992336553988102L;

	private int uID;

	private boolean protocollato;
	private String numeroPg;
	private String annoPg;

	private String nome;
	private String versione;
	private Date dataCaricamento;

	private String stato;
	private String clientID;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + uID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ElementoComposizione))
			return false;
		ElementoComposizione other = (ElementoComposizione) obj;
		if (uID != other.uID)
			return false;
		return true;
	}

}
