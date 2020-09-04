package it.eng.portlet.consolepec.gwt.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author biagiot
 *
 */
public class CampoMetadatoDTO implements IsSerializable{
	private String idMetadato, etichettaMetadato;

	public CampoMetadatoDTO() {

	}

	public CampoMetadatoDTO(String idMetadato, String etichettaMetadato) {
		this.idMetadato = idMetadato;
		this.etichettaMetadato = etichettaMetadato;
	}

	public String getIdMetadato() {
		return idMetadato;
	}

	public void setIdMetadato(String idMetadato) {
		this.idMetadato = idMetadato;
	}

	public String getEtichettaMetadato() {
		return etichettaMetadato;
	}

	public void setEtichettaMetadato(String etichettaMetadato) {
		this.etichettaMetadato = etichettaMetadato;
	}
}
