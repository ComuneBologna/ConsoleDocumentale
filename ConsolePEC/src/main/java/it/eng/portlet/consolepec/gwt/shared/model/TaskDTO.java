package it.eng.portlet.consolepec.gwt.shared.model;

import it.eng.portlet.consolepec.gwt.shared.model.DatiTaskDTO.TipoTaskDTO;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author biagiot
 *
 */
public class TaskDTO<T extends DatiTaskDTO> implements IsSerializable{

	protected TipoTaskDTO tipoTask;
	protected T dati;
	protected String currentUser;
	protected boolean utenteEsterno;

	public String getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

	public boolean isUtenteEsterno() {
		return utenteEsterno;
	}

	public void setUtenteEsterno(boolean utenteEsterno) {
		this.utenteEsterno = utenteEsterno;
	}

	public T getDati() {
		return dati;
	}

	public TipoTaskDTO getTipo() {
		return tipoTask;
	}

	public TaskDTO(T dati, TipoTaskDTO tipoTask) {
		this.dati = dati;
		this.tipoTask = tipoTask;
	}

	public TaskDTO() {
		// Ser
	}

}
