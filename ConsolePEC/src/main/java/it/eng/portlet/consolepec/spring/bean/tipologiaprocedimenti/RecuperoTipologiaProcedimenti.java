package it.eng.portlet.consolepec.spring.bean.tipologiaprocedimenti;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.gwt.shared.dto.TipologiaProcedimentoDto;

import java.util.List;

public interface RecuperoTipologiaProcedimenti {

	public List<TipologiaProcedimentoDto> getElencoTipologieProcedimenti() throws SpagicClientException;

	String getDescrizioneProcedimento(int codiceProcedimento);

}
