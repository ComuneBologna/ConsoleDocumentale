package it.eng.portlet.consolepec.gwt.shared.action;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class RecuperaGruppiVisibilita extends LiferayPortletUnsecureActionImpl<RecuperaGruppiVisibilitaResult> {
	
	@Getter
	@Setter
	private TipologiaPratica tipologiaPratica;
}
