package it.eng.portlet.consolepec.gwt.shared.action.configurazioni.amministrazione;

import java.util.HashMap;
import java.util.Map;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author biagiot
 *
 */
public class AmministrazioneAbilitazioniRuoloAction extends LiferayPortletUnsecureActionImpl<AmministrazioneAbilitazioniRuoloResult> {

	@Setter
	@Getter
	private Map<String, Object> filtriRicerca = new HashMap<String, Object>();

	@Getter
	@Setter
	private Integer limit;

	@Getter
	@Setter
	private Integer offset;

	@Getter
	private String orderBy;

	@Getter
	private Boolean sort;

	@Getter
	Operazione operazione;

	public AmministrazioneAbilitazioniRuoloAction() {
		this.operazione = Operazione.CARICAMENTO;
	}

	public AmministrazioneAbilitazioniRuoloAction(Map<String, Object> filtriRicerca, Integer limit, Integer offset, String orderBy, Boolean sort) {
		this.filtriRicerca = filtriRicerca;
		this.limit = limit;
		this.offset = offset;
		this.orderBy = orderBy;
		this.sort = sort;
		this.operazione = Operazione.AGGREGAZIONE;
	}

	public static enum Operazione {
		CARICAMENTO, AGGREGAZIONE;
	}

}
