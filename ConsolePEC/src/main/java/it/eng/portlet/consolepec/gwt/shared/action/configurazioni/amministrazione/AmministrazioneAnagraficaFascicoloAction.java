package it.eng.portlet.consolepec.gwt.shared.action.configurazioni.amministrazione;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.Azione;
import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 
 * @author biagiot
 *
 */
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class AmministrazioneAnagraficaFascicoloAction extends LiferayPortletUnsecureActionImpl<AmministrazioneAnagraficaFascicoloResult>{
	
	@Getter
	private Map<String, Object> filtri = new HashMap<String, Object>();
	
	@Getter
	private Integer limit;
	
	@Getter
	private Integer offset;
	
	@Getter
	private String orderBy;
	
	@Getter
	private Boolean sort;
	
	@Getter
	private AnagraficaFascicolo anagraficaFascicolo;
	
	@Getter
	private List<Azione> azioni = new ArrayList<>();
	
	@Getter
	private Operazione operazione;
	
	public AmministrazioneAnagraficaFascicoloAction(Operazione operazione, AnagraficaFascicolo ai, List<Azione> azioni) {
		this.operazione = operazione;
		this.anagraficaFascicolo = ai;
		this.azioni = azioni;
	}
	
	public AmministrazioneAnagraficaFascicoloAction(Map<String, Object> filtri, Integer limit, Integer offset, String orderBy, Boolean sort) {
		this.filtri = filtri;
		this.limit = limit;
		this.offset = offset;
		this.operazione = Operazione.RICERCA;
		this.orderBy = orderBy;
		this.sort = sort;
	}
	
	public static enum Operazione {
		RICERCA,
		MODIFICA,
		INSERIMENTO;
	}
	
}
