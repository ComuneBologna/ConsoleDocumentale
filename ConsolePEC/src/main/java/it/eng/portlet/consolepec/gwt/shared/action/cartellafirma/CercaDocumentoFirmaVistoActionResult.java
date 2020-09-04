package it.eng.portlet.consolepec.gwt.shared.action.cartellafirma;

import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DocumentoFirmaVistoDTO;

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
public class CercaDocumentoFirmaVistoActionResult implements Result {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public CercaDocumentoFirmaVistoActionResult() {
	}

	private List<DocumentoFirmaVistoDTO> documentiFirmaVisto;
	private String errorMessage;
	private boolean error;
	private int maxResult;
	private boolean estimate;

}
