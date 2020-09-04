package it.eng.portlet.consolepec.gwt.shared.model.cartellafirma;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author biagiot
 *
 */

@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of="userId")
public class DestinatarioUtenteDTO extends DestinatarioDTO implements Comparable<DestinatarioUtenteDTO>{

	private String userId;
	private String nome;
	private String cognome;
	private String nomeCompleto;
	private String settore;
	private String matricola;

	@Override
	public int compareTo(DestinatarioUtenteDTO o) {
		return userId.compareTo(o.getUserId());
	}
}
