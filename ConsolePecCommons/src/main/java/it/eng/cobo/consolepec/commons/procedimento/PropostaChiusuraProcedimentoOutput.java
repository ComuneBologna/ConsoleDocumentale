package it.eng.cobo.consolepec.commons.procedimento;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class PropostaChiusuraProcedimentoOutput implements Serializable {

	private static final long serialVersionUID = 3081607613779471309L;

	private String idDocumentaleFascicolo;
	private List<ProcedimentoDaChiudere> procedimentiProposti = new ArrayList<ProcedimentoDaChiudere>();

	@Data
	public static class ProcedimentoDaChiudere {
		private String tipoRecord;
		private String tipoProtocolloCapofila;
		private String annoProtocolloCapofila;
		private String numeroProtocolloCapofila;
		private Integer codTipologiaProcedimento;
		private Integer codQuartiere;
		private Date dataAvvio;
		private Date dataInizioDecorrenza;
		private String primaRigaDescrizione;
		private String secondaRigaDescrizione;
		private String terzaRigaDescrizione;
		private String quartaRigaDescrizione;
	}
}
