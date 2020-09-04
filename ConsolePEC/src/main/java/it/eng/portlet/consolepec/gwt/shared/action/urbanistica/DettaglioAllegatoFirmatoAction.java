package it.eng.portlet.consolepec.gwt.shared.action.urbanistica;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author GiacomoFM
 * @since 13/feb/2018
 */
@NoArgsConstructor
@AllArgsConstructor
public class DettaglioAllegatoFirmatoAction extends LiferayPortletUnsecureActionImpl<DettaglioAllegatoFirmatoResult> {

	@Getter private String uuidAlfresco;

}
