package it.eng.portlet.consolepec.gwt.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class EventoIterDTO implements IsSerializable {

	private String notaEvento;
	private String dateTimeEvento;
	
	public EventoIterDTO() {
		// TODO Auto-generated constructor stub
	}

	public EventoIterDTO(String notaEvento, String dateTimeEvento) {
		super();
		this.notaEvento = notaEvento;
		this.dateTimeEvento = dateTimeEvento;
	}

	public String getDateTimeEvento() {
		return dateTimeEvento;
	}

	public void setDateTimeEvento(String dateTimeEvento) {
		this.dateTimeEvento = dateTimeEvento;
	}

	public String getNotaEvento() {
		return notaEvento;
	}

	public void setNotaEvento(String notaEvento) {
		this.notaEvento = notaEvento;
	}

}
