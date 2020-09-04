package it.eng.portlet.consolepec.gwt.shared.action.pec;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SalvaBozzaInvio extends LiferayPortletUnsecureActionImpl<SalvaBozzaInvioResult> {

	private PecOutDTO bozzaPecOut;
	private String idFascicolo;
	private String idEmailOut;

}
