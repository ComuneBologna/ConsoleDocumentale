package it.eng.portlet.consolepec.gwt.shared.action.urbanistica;

import it.eng.cobo.consolepec.commons.firmadigitale.FirmaDigitale;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.gwtplatform.dispatch.shared.Result;

/**
 * @author GiacomoFM
 * @since 13/feb/2018
 */
@NoArgsConstructor
public class DettaglioAllegatoFirmatoResult implements Result {

	private static final long serialVersionUID = -6330584808383567132L;

	@Getter private FirmaDigitale firmaDigitale;

	@Getter private String msgError;
	@Getter private boolean error = false;

	public DettaglioAllegatoFirmatoResult(FirmaDigitale firmaDigitale) {
		super();
		this.firmaDigitale = firmaDigitale;
	}

	public DettaglioAllegatoFirmatoResult(String msgError) {
		super();
		this.error = true;
		this.msgError = msgError;
	}

}
