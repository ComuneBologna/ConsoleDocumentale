package it.eng.portlet.consolepec.gwt.shared.drive.action;

import com.gwtplatform.dispatch.shared.Result;

import it.eng.cobo.consolepec.commons.drive.File;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Giacomo F.M.
 * @since 2019-06-12
 */
@NoArgsConstructor
public class AggiornaFileResult implements Result {

	private static final long serialVersionUID = 3942059398520816088L;

	@Getter
	private File file;

	@Getter
	private String msgError = "";
	@Getter
	private boolean error = false;

	public AggiornaFileResult(File file) {
		super();
		this.file = file;
	}

	public AggiornaFileResult(String msgError) {
		super();
		this.error = true;
		this.msgError = msgError;
	}

}
