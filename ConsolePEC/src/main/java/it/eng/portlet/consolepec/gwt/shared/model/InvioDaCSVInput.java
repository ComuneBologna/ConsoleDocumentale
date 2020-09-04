package it.eng.portlet.consolepec.gwt.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

import lombok.Data;

@Data
public class InvioDaCSVInput implements IsSerializable {

	private String nomeAllegatoCSV;
	private String idDocumentaleFascicolo;

	private Character separatoreCSV;
	private Integer headerCSV;

	private String idDocumentaleTemplate;

	private Integer posizioneIndirizzoEmailDestinatario;
	private Integer posizioneIdDocumentaleFascicolo;

	private boolean indirizzoDestinatarioFromModello;

	private String assegnatario;
	private boolean preValidazione;

}
