package it.eng.portlet.consolepec.gwt.client.presenter.rubrica.command;

import it.eng.portlet.consolepec.gwt.client.presenter.event.rubrica.SelezionaAnagraficaInizioEvent;

import com.google.gwt.user.client.Command;
import com.google.web.bindery.event.shared.EventBus;

public class SelezionaAnagraficaCommand implements Command {

	EventBus eventBus;
	Object openingRequestor;
	String nomeDatoAggiuntivo;

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public Object getOpeningRequestor() {
		return openingRequestor;
	}

	public void setOpeningRequestor(Object openingRequestor) {
		this.openingRequestor = openingRequestor;
	}

	public SelezionaAnagraficaCommand(EventBus eventBus, Object openingRequestor) {
		this.eventBus = eventBus;
		this.openingRequestor = openingRequestor;
	}
	
	public SelezionaAnagraficaCommand() {
		this.eventBus = null;
		this.openingRequestor = null;
	}
	
	
	@Override
	public void execute() {
		SelezionaAnagraficaInizioEvent event = new SelezionaAnagraficaInizioEvent(openingRequestor);
		event.setNomeDatoAggiuntivo(nomeDatoAggiuntivo);
		eventBus.fireEvent(event);
	}
	
	public void setNomeDatoAggiuntivo(String nomeDatoAggiuntivo) {
		this.nomeDatoAggiuntivo = nomeDatoAggiuntivo;
	}
	
	public String getNomeDatoAggiuntivo() {
		return nomeDatoAggiuntivo;
	}
}
