package it.eng.portlet.consolepec.gwt.shared.action.rubrica;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author GiacomoFM
 * @since 14/nov/2018
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ImportaLagAction extends LiferayPortletUnsecureActionImpl<ImportaLagResult> {

	@Getter private String codiceFiscale;

}
