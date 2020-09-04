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
@EqualsAndHashCode(callSuper = false, of="nomeGruppoConsole")
public class DestinatarioGruppoDTO extends DestinatarioDTO implements Comparable<DestinatarioGruppoDTO>{

	private String nomeGruppoConsole;
	private String nomeGruppoDisplay;

	@Override
	public int compareTo(DestinatarioGruppoDTO o) {
		return nomeGruppoConsole.compareTo(o.getNomeGruppoConsole());
	}
}
