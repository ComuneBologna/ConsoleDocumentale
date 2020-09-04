package it.eng.portlet.consolepec.gwt.shared.action.configurazioni;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author biagiot
 *
 */
@NoArgsConstructor
public class CaricaAnagrafichePraticheAction extends LiferayPortletUnsecureActionImpl<CaricaAnagrafichePraticheResult> {

	@Getter
	@Setter
	private boolean ricarica = false;

}
