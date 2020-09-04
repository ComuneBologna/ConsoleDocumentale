package it.eng.cobo.consolepec.commons.appc.procedimento;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * @author Giacomo F.M.
 * @since 2019-12-13
 */
@Data
public class PropostaChiusuraProcedimentoResponse implements Serializable {

	private static final long serialVersionUID = -3330994715452922613L;

	private String codiceMessaggio;
	private String descrizioneMessaggio;

	List<ProcedimentoAvviato> records = new ArrayList<>();

	@Data
	public static class ProcedimentoAvviato implements Serializable {
		private static final long serialVersionUID = -1640674582224476013L;

		private String tipoProtocolloCapofila;
		private String annoProtocolloCapofila;
		private String numeroProtocolloCapofila;
		private Integer codiceTipologiaProcedimento;
		private Integer codiceQuartiere;
		private String dataAvvioProcedimento;
		private String dataInizioDecorrenzaProcedimento;
		private String primaRigaDescrizioneProcedimento;
		private String secondaRigaDescrizioneProcedimento;
		private String terzaRigaDescrizioneProcedimento;
		private String quartaRigaDescrizioneProcedimento;
		private String libero;
	}

}
