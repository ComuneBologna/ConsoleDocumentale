package it.eng.portlet.consolepec.gwt.shared.action.urbanistica;

import java.util.HashMap;
import java.util.Map;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author GiacomoFM
 * @since 06/nov/2017
 */
@NoArgsConstructor
@AllArgsConstructor
public class RicercaPraticaProcediAction extends LiferayPortletUnsecureActionImpl<RicercaPraticaProcediResult> {

	@Getter
	private Map<String, Object> filtri = new HashMap<>();
	@Getter
	private Map<String, Boolean> ordinamento = new HashMap<>();
	@Setter
	@Getter
	private Integer limit;
	@Setter
	@Getter
	private Integer offset;

}
