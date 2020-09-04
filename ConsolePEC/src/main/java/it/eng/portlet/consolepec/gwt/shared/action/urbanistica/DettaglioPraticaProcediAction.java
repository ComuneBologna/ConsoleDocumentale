package it.eng.portlet.consolepec.gwt.shared.action.urbanistica;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author GiacomoFM
 * @since 07/nov/2017
 */
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class DettaglioPraticaProcediAction extends LiferayPortletUnsecureActionImpl<DettaglioPraticaProcediResult> {

	@Getter private List<String> ids;
	
	public DettaglioPraticaProcediAction(List<String> ids) {
		this.ids = ids;
	}
}
