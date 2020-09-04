package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author GiacomoFM
 * @since 13/mar/2019
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UploadFileZipAction extends LiferayPortletUnsecureActionImpl<UploadFileZipResult> {

	@Getter
	private String pathFascicolo;
	@Getter
	private String tmpDir, tmpFile;

}
