package it.eng.portlet.consolepec.gwt.shared.action;

import lombok.Getter;

/**
 *
 * @author biagiot
 *
 */

@Getter
public class EliminaFileDaTmpAction extends LiferayPortletUnsecureActionImpl<EliminaFileDaTmpResult> {

	private String pathFile;

	public EliminaFileDaTmpAction(String pathFile) {
		this.pathFile = pathFile;
	}

	protected EliminaFileDaTmpAction() {
		// Ser
	}
}
