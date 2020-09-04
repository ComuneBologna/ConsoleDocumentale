package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class CreaFascicoloAction extends LiferayPortletUnsecureActionImpl<CreaFascicoloActionResult> {

	@Getter
	@Setter
	private String titolo;
	
	@Getter
	@Setter
	private String utente;
	
	@Getter
	@Setter
	private String note;
	
	@Getter
	@Setter
	private String clientID;
	
	@Getter
	@Setter
	private TipologiaPratica tipologiaFascicolo;
	
	@Getter
	@Setter
	private String assegnatario;
	
	@Getter
	private List<DatoAggiuntivo> datiAggiuntivi = new ArrayList<>();
}
