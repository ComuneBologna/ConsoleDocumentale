package it.eng.portlet.consolepec.gwt.shared.action;

import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author GiacomoFM
 * @since 13/lug/2017
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ModificaFascicoloAction extends LiferayPortletUnsecureActionImpl<ModificaFascicoloResult> {

	@Getter
	private PraticaDTO pratica;

	@Getter
	private String titolo;

	@Getter
	private String tipoFascicolo;
	
	@Getter
	private List<DatoAggiuntivo> datiAggiuntivi;

}
