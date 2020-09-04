package it.eng.portlet.consolepec.spring.bean.search;

import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;
import it.eng.portlet.consolepec.gwt.shared.action.cartellafirma.CercaDocumentoFirmaVistoAction;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DocumentoFirmaVistoDTO;

import java.util.List;

public interface NewMongoDbSearchAdapter {

	CountResponse countPratiche(CercaPratiche cercaPraticheAction);
	List<PraticaDTO> searchPratiche(CercaPratiche cercaPraticheAction);

	CountResponse countDocumentiFirmaVisto(CercaDocumentoFirmaVistoAction documentoFirmaVistoAction);
	List<DocumentoFirmaVistoDTO> searchDocumentiFirmaVisto(CercaDocumentoFirmaVistoAction documentoFirmaVistoAction);

	public static class CountResponse {
		private final int count;
		private final boolean isEstimate;

		public CountResponse(int count, boolean isEstimate) {
			this.count = count;
			this.isEstimate = isEstimate;
		}

		public int getCount() {
			return count;
		}

		public boolean isEstimate() {
			return isEstimate;
		}

	}
}
