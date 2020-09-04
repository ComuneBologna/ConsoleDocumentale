package it.eng.portlet.consolepec.gwt.shared.action;

import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;

import java.io.Serializable;
import java.util.List;


public class ValidazioneDatiAggiuntivi extends LiferayPortletUnsecureActionImpl<ValidazioneDatiAggiuntiviResult> implements Serializable{

private static final long serialVersionUID = 1L;

	List<DatoAggiuntivo> datiAggiuntivi;

	@SuppressWarnings("unused")
	private ValidazioneDatiAggiuntivi() {
		// 	For serialization only
	}

	public ValidazioneDatiAggiuntivi(List<DatoAggiuntivo> datiAggiuntivi) {
		super();
		this.datiAggiuntivi = datiAggiuntivi;
	}

	public List<DatoAggiuntivo> getDatiAggiuntivi() {
		return datiAggiuntivi;
	}

}
