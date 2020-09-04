package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ChiusuraProcedimentoResult implements GestioneProcedimentoResult {

	private static final long serialVersionUID = -3386186043342808449L;

	private String fascicoloPath;
	private String errorMsg;
	private boolean error;
	private String descrizioneProcedimento;
	private int codiceProcedimento;
	private Date dataInizioProcedimento;
	private Date dataChiusuraProcedimento;
	private Integer tempoNormatoInGiorni;
	private Integer durataProcedimento;

	public ChiusuraProcedimentoResult(String errorMessage) {
		this.error = true;
		this.errorMsg = errorMessage;
	}

	public ChiusuraProcedimentoResult(String fascicoloPath, String descrizioneProcedimento, int codiceProcedimento, Date dataInizioProcedimento, Date dataChiusuraProcedimento,
			Integer tempoNormatoInGiorni, Integer durataProcedimento) {
		super();
		this.fascicoloPath = fascicoloPath;
		this.descrizioneProcedimento = descrizioneProcedimento;
		this.codiceProcedimento = codiceProcedimento;
		this.dataInizioProcedimento = dataInizioProcedimento;
		this.dataChiusuraProcedimento = dataChiusuraProcedimento;
		this.tempoNormatoInGiorni = tempoNormatoInGiorni;
		this.durataProcedimento = durataProcedimento;
		this.error = false;
		this.errorMsg = null;
	}
}
