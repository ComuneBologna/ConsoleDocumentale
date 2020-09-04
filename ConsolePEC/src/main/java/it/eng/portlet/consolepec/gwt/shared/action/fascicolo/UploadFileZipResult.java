package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import com.gwtplatform.dispatch.shared.Result;

import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author GiacomoFM
 * @since 13/mar/2019
 */
@NoArgsConstructor
public class UploadFileZipResult implements Result {

	private static final long serialVersionUID = 5259380306164090648L;

	@Getter
	private FascicoloDTO fascicoloDTO;

	@Getter
	private String msgError = "";
	@Getter
	private boolean error = false;

	public UploadFileZipResult(FascicoloDTO fascicoloDTO) {
		super();
		this.fascicoloDTO = fascicoloDTO;
	}

	public UploadFileZipResult(String msgError) {
		super();
		this.error = true;
		this.msgError = msgError;
	}
}
