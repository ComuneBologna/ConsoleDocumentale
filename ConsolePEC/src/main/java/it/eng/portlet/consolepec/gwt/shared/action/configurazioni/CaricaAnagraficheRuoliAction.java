package it.eng.portlet.consolepec.gwt.shared.action.configurazioni;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class CaricaAnagraficheRuoliAction extends LiferayPortletUnsecureActionImpl<CaricaAnagraficheRuoliResult> {

	@Getter
	@Setter
	private boolean ricarica = false;

}
