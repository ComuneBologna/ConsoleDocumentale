package it.eng.portlet.consolepec.gwt.shared.action.rubrica;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author GiacomoFM
 * @since 18/set/2017
 */
@NoArgsConstructor
public class RicercaAnagrafiche extends LiferayPortletUnsecureActionImpl<RicercaAnagraficheResult> {

	@Getter private Map<String, Object> filtri = new HashMap<>();

}
