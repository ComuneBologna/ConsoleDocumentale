package it.eng.portlet.consolepec.gwt.shared.action;

import it.eng.portlet.consolepec.gwt.shared.model.TmpFileUploadDTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author biagiot
 *
 */

@Getter
@Setter
@AllArgsConstructor
public class RecuperaFileDaTmpAction extends LiferayPortletUnsecureActionImpl<RecuperaFileDaTmpResult>{

	private List<TmpFileUploadDTO> tempFiles;

	protected RecuperaFileDaTmpAction() {
		// Ser
	}
}
