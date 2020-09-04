package it.eng.portlet.consolepec.gwt.shared.action.profilazione;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
@AllArgsConstructor
public class CaricaWorklistAction extends LiferayPortletUnsecureActionImpl<CaricaWorklistResult> {
	
	@Getter 
	private boolean ricaricaContatori;
}
