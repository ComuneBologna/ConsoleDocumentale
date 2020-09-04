package it.eng.portlet.consolepec.gwt.shared.action.profilazione;

import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente;
import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente.PreferenzeFirmaDigitale;
import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente.PreferenzeRiassegnazione;
import lombok.Getter;
import lombok.Setter;

import com.gwtplatform.dispatch.shared.Result;

/**
 * 
 * @author biagiot
 *
 */
public class GestionePreferenzeUtenteResult implements Result {
	
	private static final long serialVersionUID = 1L;
	
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
	private boolean error;
	
	@Getter
	private String errorMessage;
	
	public GestionePreferenzeUtenteResult(PreferenzeUtente preferenzeUtente, PreferenzeFirmaDigitale preferenzeFirmaDigitale, PreferenzeRiassegnazione preferenzeRiassegnazione) {
		this.preferenzeUtente = preferenzeUtente;
		this.preferenzeFirmaDigitale = preferenzeFirmaDigitale;
		this.preferenzeRiassegnazione = preferenzeRiassegnazione;
		this.error = false;
		this.errorMessage = null;
	}
	
	public GestionePreferenzeUtenteResult() {
		this.error = false;
		this.errorMessage = null;
	}
	
	public GestionePreferenzeUtenteResult(String errorMessage) {
		this.error = true;
		this.errorMessage = errorMessage;
	}
}
