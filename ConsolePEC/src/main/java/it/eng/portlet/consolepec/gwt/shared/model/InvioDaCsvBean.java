package it.eng.portlet.consolepec.gwt.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

import lombok.Data;

@Data
public class InvioDaCsvBean implements IsSerializable {

	private String clientIdFascicolo;
	private String nomeAllegato;

	private Character separatoreCSV;
	private Integer headerCSV;

	private String clientIdTemplate;

	private Integer posizioneIndirizzoEmailDestinatario;
	private Integer posizioneIdDocumentaleFascicolo;

	private boolean indirizzoDestinatarioFromModello;

	private String assegnatario;
	private boolean preValidazione;
}
