package it.eng.portlet.consolepec.gwt.shared.action.profilazione;

import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente;
import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente.PreferenzeFirmaDigitale;
import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente.PreferenzeRiassegnazione;
import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author biagiot
 *
 */
public class GestionePreferenzeUtenteAction extends LiferayPortletUnsecureActionImpl<GestionePreferenzeUtenteResult> {
	
	@Getter
	@Setter
	private PreferenzeUtente preferenzeUtente;
	
	@Getter
	@Setter
	private PreferenzeFirmaDigitale preferenzeFirmaDigitale;
	
	@Getter
	@Setter
	private PreferenzeRiassegnazione preferenzeRiassegnazione;
	
	@Getter
	private TipoOperazione tipoOperazione;
		
	
	public GestionePreferenzeUtenteAction(PreferenzeUtente preferenzeUtente, TipoOperazione tipoOperazione) {
		this.preferenzeUtente = preferenzeUtente;
		this.tipoOperazione = tipoOperazione;
	}
	
	public GestionePreferenzeUtenteAction(PreferenzeFirmaDigitale preferenzeFirmaDigitale, TipoOperazione tipoOperazione) {
		this.preferenzeFirmaDigitale = preferenzeFirmaDigitale;
		this.tipoOperazione = tipoOperazione;
	}
	
	public GestionePreferenzeUtenteAction(PreferenzeRiassegnazione preferenzeRiassegnazione, TipoOperazione tipoOperazione) {
		this.preferenzeRiassegnazione = preferenzeRiassegnazione;
		this.tipoOperazione = tipoOperazione;
	}
	
	public GestionePreferenzeUtenteAction() {
		this.tipoOperazione = TipoOperazione.CARICA;
	}
	
	public enum TipoOperazione {
		CARICA, AGGIORNA, ELIMINA;
	}
}
