package it.eng.portlet.consolepec.gwt.shared.action.configurazioni.amministrazione;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.Azione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.Abilitazione;
import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 
 * @author biagiot
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AmministrazioneAnagraficaRuoloAction extends LiferayPortletUnsecureActionImpl<AmministrazioneAnagraficaRuoloResult> {

	@Getter
	private Operazione operazione;

	@Getter
	private Map<String, Object> filtriRicerca = new HashMap<String, Object>();

	@Getter
	private Integer limit;

	@Getter
	private Integer offset;

	@Getter
	private String orderBy;

	@Getter
	private Boolean sort;

	@Getter
	private AnagraficaRuolo anagraficaRuolo;

	@Getter
	private String settore;

	@Getter
	private List<Abilitazione> abilitazioni;

	@Getter
	private List<Abilitazione> abilitazioniDaRimuovere;

	@Getter
	private List<Azione> azioniRuolo;

	@Getter
	private List<Azione> azioniAbilitazioni;

	public AmministrazioneAnagraficaRuoloAction(AnagraficaRuolo anagraficaRuolo, String settore, List<Abilitazione> abilitazioni, List<Azione> azioniRuolo, List<Azione> azioniAbilitazioni) {
		this.operazione = Operazione.INSERIMENTO;
		this.anagraficaRuolo = anagraficaRuolo;
		this.abilitazioni = abilitazioni;
		this.settore = settore;
		this.azioniRuolo = azioniRuolo;
		this.azioniAbilitazioni = azioniAbilitazioni;
	}

	public AmministrazioneAnagraficaRuoloAction(AnagraficaRuolo anagraficaRuolo, String settore, List<Abilitazione> abilitazioniDaAggiungere, List<Abilitazione> abilitazioniDaRimuovere,
			List<Azione> azioniRuolo, List<Azione> azioniAbilitazioni) {
		this.operazione = Operazione.MODIFICA;
		this.anagraficaRuolo = anagraficaRuolo;
		this.abilitazioni = abilitazioniDaAggiungere;
		this.abilitazioniDaRimuovere = abilitazioniDaRimuovere;
		this.settore = settore;
		this.azioniRuolo = azioniRuolo;
		this.azioniAbilitazioni = azioniAbilitazioni;
	}

	public AmministrazioneAnagraficaRuoloAction(Map<String, Object> filtriRicerca, Integer limit, Integer offset, String orderBy, Boolean sort) {
		this.operazione = Operazione.RICERCA;
		this.limit = limit;
		this.offset = offset;
		this.filtriRicerca = filtriRicerca;
		this.orderBy = orderBy;
		this.sort = sort;
	}

	public static enum Operazione {
		INSERIMENTO, RICERCA, MODIFICA;
	}

}
