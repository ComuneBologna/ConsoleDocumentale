package it.eng.portlet.consolepec.gwt.shared.action.pec;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
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
public class EstraiEMLAction extends LiferayPortletUnsecureActionImpl<EstraiEMLResult>{

	private String pecPath;
	private String praticaPath;

	protected EstraiEMLAction() {
		// SER
	}
}
