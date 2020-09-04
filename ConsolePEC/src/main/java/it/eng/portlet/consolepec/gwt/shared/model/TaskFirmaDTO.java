package it.eng.portlet.consolepec.gwt.shared.model;

import it.eng.portlet.consolepec.gwt.shared.model.DatiTaskDTO.TipoTaskDTO;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author biagiot
 *
 */
public class TaskFirmaDTO extends TaskDTO<DatiTaskFirmaDTO> implements IsSerializable{

	public TaskFirmaDTO(DatiTaskFirmaDTO dati) {
		super(dati, TipoTaskDTO.RICHIESTA_FIRMA_TASK);
	}

	protected TaskFirmaDTO() {
		super();
		// Ser
	}
}
