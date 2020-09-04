package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import java.util.Date;

import it.eng.cobo.consolepec.commons.procedimento.ChiusuraProcedimentoInput.ModalitaChiusuraProcedimento;
import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ChiusuraProcedimentoAction extends LiferayPortletUnsecureActionImpl<ChiusuraProcedimentoResult> {

	private String idDocumentaleFascicoloConProcedimento;
	private int annoProtocollazione;
	private String numProtocollazione;
	private int codTipologiaProcedimento;
	private ModalitaChiusuraProcedimento modalitaChiusura;
	private Date dataChiusura;
	private String numProtocolloDocChiusura;
	private Integer annoProtocolloDocChiusura;
	private Date dataInizio;
	private int termine;

	private String fascicoloCorrentePath;
}
