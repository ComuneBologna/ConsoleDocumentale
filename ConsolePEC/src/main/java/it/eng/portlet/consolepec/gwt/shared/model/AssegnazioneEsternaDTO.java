package it.eng.portlet.consolepec.gwt.shared.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AssegnazioneEsternaDTO implements IsSerializable {

	private List<String> destinatari = new ArrayList<String>();
	private String testoMail;
	private List<String> operazioni = new ArrayList<String>();
	private String dataNotifica;
	
	public List<String> getDestinatari() {
		return destinatari;
	}
	public String getTestoMail() {
		return testoMail;
	}
	public List<String> getOperazioni() {
		return operazioni;
	}
	public void setTestoMail(String testoEmail) {
		this.testoMail = testoEmail;
	}
	public String getDataNotifica() {
		return dataNotifica;
	}
	public void setDataNotifica(String dataNotifica) {
		this.dataNotifica = dataNotifica;
	}
	
	
}
