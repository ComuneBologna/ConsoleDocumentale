package it.eng.portlet.consolepec.gwt.shared.model;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CapofilaFromBA01DTO implements IsSerializable {

	private String oggetto;
	private String numeroPg;
	private String annoPg;
	private boolean isNew;
	private Date dataProtocollazione;
	
	public CapofilaFromBA01DTO(String oggetto, String numeroPg, String annoPg, boolean isNew, Date dataProtocollazione) {
		this.oggetto = oggetto;
		this.numeroPg = numeroPg;
		this.annoPg = annoPg;
		this.isNew = isNew;
		this.dataProtocollazione = dataProtocollazione;
	}

	protected CapofilaFromBA01DTO() {
	}

	public String getOggetto() {
		return oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public String getNumeroPg() {
		return numeroPg;
	}

	public void setNumeroPg(String numeroPg) {
		this.numeroPg = numeroPg;
	}

	public String getAnnoPg() {
		return annoPg;
	}

	public void setAnnoPg(String annoPg) {
		this.annoPg = annoPg;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public Date getDataProtocollazione() {
		return dataProtocollazione;
	}

	public void setDataProtocollazione(Date dataProtocollazione) {
		this.dataProtocollazione = dataProtocollazione;
	}

	
}
