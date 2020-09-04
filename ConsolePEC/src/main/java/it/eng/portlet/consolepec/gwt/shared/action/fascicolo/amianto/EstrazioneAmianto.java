package it.eng.portlet.consolepec.gwt.shared.action.fascicolo.amianto;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;

import java.util.Date;

public class EstrazioneAmianto extends LiferayPortletUnsecureActionImpl<EstrazioneAmiantoResult> {

	private Date inizio;
	private Date fine;
	
	public EstrazioneAmianto() {
	}

	public EstrazioneAmianto(Date inizio, Date fine) {
		super();
		this.inizio = inizio;
		this.fine = fine;
	}

	public Date getInizio() {
		return inizio;
	}

	public Date getFine() {
		return fine;
	}



}
