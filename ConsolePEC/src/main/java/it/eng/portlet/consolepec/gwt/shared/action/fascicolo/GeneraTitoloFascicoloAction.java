package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GeneraTitoloFascicoloAction extends LiferayPortletUnsecureActionImpl<GeneraTitoloFascicoloResult> {
	
	@Getter
	private CreaFascicoloDTO creaFascicoloDTO;
}
