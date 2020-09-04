package it.eng.portlet.consolepec.gwt.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Informazioni di firma dettagliate, ritornate dal servizio particolare. Al
 * momento non ridefinisco equals() hashcode() in quanto non conosco i campi
 * identificativi.
 * 
 * @author pluttero
 * 
 */
public class InformazioniFirmaDTO implements IsSerializable {
	String validoDal, validoAl, ca, dataFirma, status, descr, dn;

	public InformazioniFirmaDTO() {
		// ser
	}

	public String getValidoDal() {
		return validoDal;
	}

	public void setValidoDal(String validoDal) {
		this.validoDal = validoDal;
	}

	public String getValidoAl() {
		return validoAl;
	}

	public void setValidoAl(String validoAl) {
		this.validoAl = validoAl;
	}

	public String getCa() {
		return ca;
	}

	public void setCa(String ca) {
		this.ca = ca;
	}

	public String getDataFirma() {
		return dataFirma;
	}

	public void setDataFirma(String dataFirma) {
		this.dataFirma = dataFirma;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getDn() {
		return dn;
	}

	public void setDn(String dn) {
		this.dn = dn;
	}

}