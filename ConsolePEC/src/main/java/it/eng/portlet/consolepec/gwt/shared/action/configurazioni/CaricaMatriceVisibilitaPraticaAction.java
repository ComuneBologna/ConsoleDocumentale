package it.eng.portlet.consolepec.gwt.shared.action.configurazioni;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 
 * @author biagiot
 *
 */
@NoArgsConstructor(access=AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class CaricaMatriceVisibilitaPraticaAction extends LiferayPortletUnsecureActionImpl<CaricaMatriceVisibilitaPraticaResult>{
	
	private TipologiaPratica tipoPratica;
}
