package it.eng.portlet.consolepec.gwt.shared.action.configurazioni.amministrazione;

import java.util.HashMap;
import java.util.Map;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaIngresso;
import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author biagiot
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AmministrazioneAnagraficaIngressoAction extends LiferayPortletUnsecureActionImpl<AmministrazioneAnagraficaIngressoResult> {

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
	private AnagraficaIngresso anagraficaIngresso;

	@Getter
	private Operazione operazione;

	@Getter
	private String tipologiaEmail;

	@Getter
	private String indirizzoEmail;

	@Getter
	private String nuovoAssegnatario;

	@Getter
	@Setter
	private boolean creaEmailOut;

	public AmministrazioneAnagraficaIngressoAction(Operazione operazione, AnagraficaIngresso ai) {
		this.operazione = operazione;
		this.anagraficaIngresso = ai;
	}

	public AmministrazioneAnagraficaIngressoAction(Map<String, Object> filtri, Integer limit, Integer offset, String orderBy, Boolean sort) {
		this.filtri = filtri;
		this.limit = limit;
		this.offset = offset;
		this.operazione = Operazione.RICERCA;
		this.orderBy = orderBy;
		this.sort = sort;
	}

	public AmministrazioneAnagraficaIngressoAction(String tipologiaEmail, String indirizzoEmail, String nuovoAssegnatario) {
		this.operazione = Operazione.AGGIORNA_PRIMO_ASSEGNATARIO;
		this.tipologiaEmail = tipologiaEmail;
		this.indirizzoEmail = indirizzoEmail;
		this.nuovoAssegnatario = nuovoAssegnatario;
	}

	public static enum Operazione {
		RICERCA, MODIFICA, INSERIMENTO, AGGIORNA_PRIMO_ASSEGNATARIO;
	}

}
