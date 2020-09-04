package it.eng.portlet.consolepec.gwt.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

import lombok.Data;

@Data
public class InvioCsvEsito implements IsSerializable {

	private String idDocumentale;
	private String indirizzoEmail;
	private boolean errore;
	private String descrizioneErrore;
	private int riga;

}
