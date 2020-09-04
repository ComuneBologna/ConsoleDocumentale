package it.eng.portlet.consolepec.gwt.shared.action.richiedifirma;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import it.eng.portlet.consolepec.gwt.shared.model.richiedifirma.RichiediFirmaVistoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author biagiot
 *
 */

@Getter
@Setter
@AllArgsConstructor
public class RichiediFirmaVistoAction extends LiferayPortletUnsecureActionImpl<RichiediFirmaVistoActionResult>{

	protected RichiediFirmaVistoAction () {
		// ser
	}

	private RichiediFirmaVistoDTO richiediFirmaVisto;

}
