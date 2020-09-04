package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import java.util.ArrayList;
import java.util.List;

import com.gwtplatform.dispatch.shared.Result;

import it.eng.portlet.consolepec.gwt.shared.model.ProcedimentoMiniDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PropostaChiusuraProcedimentiResult implements Result {

	private static final long serialVersionUID = 3863762435360790401L;

	private String idDocumentaleFascicolo;
	private List<ProcedimentoMiniDto> procedimenti = new ArrayList<ProcedimentoMiniDto>();

	private String errorMsg;
	private boolean error;

	public PropostaChiusuraProcedimentiResult(String idDocumentaleFascicolo, List<ProcedimentoMiniDto> procedimenti) {
		this.idDocumentaleFascicolo = idDocumentaleFascicolo;
		this.procedimenti = procedimenti;
		this.error = false;
	}

	public PropostaChiusuraProcedimentiResult(String errorMsg) {
		this.error = true;
		this.errorMsg = errorMsg;
	}

}
