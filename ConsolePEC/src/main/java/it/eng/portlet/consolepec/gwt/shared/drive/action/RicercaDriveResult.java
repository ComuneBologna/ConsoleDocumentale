package it.eng.portlet.consolepec.gwt.shared.drive.action;

import java.util.LinkedList;
import java.util.List;

import com.gwtplatform.dispatch.shared.Result;

import it.eng.cobo.consolepec.commons.drive.DriveElement;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Giacomo F.M.
 * @since 2019-07-12
 */
@NoArgsConstructor
public class RicercaDriveResult implements Result {

	private static final long serialVersionUID = 4986722376160450316L;

	@Getter
	private LinkedList<DriveElement> elements = new LinkedList<>();

	@Getter
	private String msgError = "";
	@Getter
	private boolean error = false;

	public RicercaDriveResult(final List<DriveElement> elements) {
		super();
		this.elements.addAll(elements);
	}

	public RicercaDriveResult(final String msgError) {
		super();
		this.error = true;
		this.msgError = msgError;
	}
}
