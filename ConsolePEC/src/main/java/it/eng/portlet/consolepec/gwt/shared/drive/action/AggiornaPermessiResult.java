package it.eng.portlet.consolepec.gwt.shared.drive.action;

import com.gwtplatform.dispatch.shared.Result;

import it.eng.cobo.consolepec.commons.drive.DriveElement;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Giacomo F.M.
 * @since 2019-07-05
 */
@NoArgsConstructor
public class AggiornaPermessiResult implements Result {

	private static final long serialVersionUID = 3052790400598496290L;

	@Getter
	private DriveElement element;

	@Getter
	private String msgError = "";
	@Getter
	private boolean error = false;

	public AggiornaPermessiResult(final DriveElement element) {
		super();
		this.element = element;
	}

	public AggiornaPermessiResult(final String msgError) {
		super();
		this.error = true;
		this.msgError = msgError;
	}

}
