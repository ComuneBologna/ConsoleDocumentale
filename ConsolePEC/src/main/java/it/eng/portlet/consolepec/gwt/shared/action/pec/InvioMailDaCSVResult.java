package it.eng.portlet.consolepec.gwt.shared.action.pec;

import java.util.ArrayList;
import java.util.List;

import com.gwtplatform.dispatch.shared.Result;

import it.eng.portlet.consolepec.gwt.shared.model.InvioCsvEsito;
import lombok.Data;

@Data
public class InvioMailDaCSVResult implements Result {

	private static final long serialVersionUID = 1191260887069502491L;

	private boolean valido;
	private List<InvioCsvEsito> esiti = new ArrayList<>();
	private String esitoFileName;

	private boolean error;
	private String errorMessage;

	boolean async;

}
