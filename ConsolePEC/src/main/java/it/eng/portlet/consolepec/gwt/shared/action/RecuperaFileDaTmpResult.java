package it.eng.portlet.consolepec.gwt.shared.action;

import it.eng.portlet.consolepec.gwt.shared.model.FileDTO;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.gwtplatform.dispatch.shared.Result;

/**
 *
 * @author biagiot
 *
 */

@Getter
@Setter
public class RecuperaFileDaTmpResult implements Result{

	private static final long serialVersionUID = 1L;

	private String errorMessage;
	private boolean error;
	private List<FileDTO> files = new ArrayList<FileDTO>();

	public RecuperaFileDaTmpResult(List<FileDTO> files) {
		this.files = files;
		this.error = false;
		this.errorMessage = null;
	}

	public RecuperaFileDaTmpResult(String errorMessage) {
		this.error = true;
		this.errorMessage = errorMessage;
	}

	protected RecuperaFileDaTmpResult() {
		// Ser
	}
}
