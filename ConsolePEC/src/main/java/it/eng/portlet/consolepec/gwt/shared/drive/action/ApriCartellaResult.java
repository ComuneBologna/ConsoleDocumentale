package it.eng.portlet.consolepec.gwt.shared.drive.action;

import java.util.LinkedList;
import java.util.List;

import com.gwtplatform.dispatch.shared.Result;

import it.eng.cobo.consolepec.commons.drive.Cartella;
import it.eng.cobo.consolepec.commons.drive.DriveElement;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Giacomo F.M.
 * @since 2019-05-31
 */
@NoArgsConstructor
public class ApriCartellaResult implements Result {

	private static final long serialVersionUID = -6281360178001330974L;

	@Getter
	private Cartella cartella;
	@Getter
	private LinkedList<DriveElement> element = new LinkedList<>();

	@Getter
	private String msgError = "";
	@Getter
	private boolean error = false;

	public ApriCartellaResult(Cartella cartella, List<DriveElement> element) {
		super();
		this.cartella = cartella;
		if (element != null) {
			this.element.clear();
			this.element.addAll(element);
		}
	}

	public ApriCartellaResult(String msgError) {
		super();
		this.error = true;
		this.msgError = msgError;
	}

}
