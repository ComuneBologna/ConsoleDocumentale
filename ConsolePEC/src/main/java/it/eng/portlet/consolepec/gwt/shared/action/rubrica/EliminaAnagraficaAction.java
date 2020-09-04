package it.eng.portlet.consolepec.gwt.shared.action.rubrica;

import it.eng.cobo.consolepec.commons.rubrica.Anagrafica;
import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author GiacomoFM
 * @since 27/ott/2017
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class EliminaAnagraficaAction extends LiferayPortletUnsecureActionImpl<EliminaAnagraficaResult> {

	@Getter private Anagrafica anagrafica;

}
