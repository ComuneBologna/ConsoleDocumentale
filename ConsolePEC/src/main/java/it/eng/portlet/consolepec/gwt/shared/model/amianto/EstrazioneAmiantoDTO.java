package it.eng.portlet.consolepec.gwt.shared.model.amianto;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class EstrazioneAmiantoDTO implements IsSerializable {

	private Date inizio;
	private Date fine;
	public void setInizio(Date inizio) {
		this.inizio = inizio;
	}
	public void setFine(Date fine) {
		this.fine = fine;
	}
	public Date getInizio() {
		return inizio;
	}
	public Date getFine() {
		return fine;
	}
	
}
