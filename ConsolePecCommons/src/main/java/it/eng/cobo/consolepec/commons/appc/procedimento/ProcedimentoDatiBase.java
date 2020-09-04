package it.eng.cobo.consolepec.commons.appc.procedimento;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @since 2019-11-21
 */
@Data
public class ProcedimentoDatiBase implements Serializable {

	private static final long serialVersionUID = 8292793111762831810L;

	private String annoProtDocChiusura;
	private String annoProtocollazione;
	private String codiceComune;
	private String codiceEvento;
	private Integer codiceProcedimento;
	private String codiceQuartiere;
	private Integer codiceStato;
	private Integer codiceUnitaOrgCompetente;
	private Integer codiceUnitaOrgResponsabile;
	private String cognomeResponsabile;
	private Date dataAnnullamentoProcedimento;
	private Date dataAvvioProcedimento;
	private Date dataChiusuraProcedimento;
	private Date dataEvento;
	private Date dataInizioDecorrenzaProcedimento;
	private Date dataInizioFaseAltraUO;
	private Date dataInizioFaseEsterna;
	private Date dataInterruzioneProcedimento;
	private Date dataSospensione;
	private Date dataStatoAttualeProcedimento;
	private String descrizioneEventoChiusura;
	private String descrizioneFaseAltraUO;
	private String descrizioneFaseEsterna;
	private String descrizioneQuartiere;
	private String descrizioneSospensione;
	private String descrizioneUnitaOrgCompetente;
	private String descrizioneUnitaOrgResponsabile;
	private String forzaturaResponsabilita;
	private Integer giorniDurataProcedimento;
	private String modalitaChiusuraProcedimento;
	private String nomeResponsabile;
	private Integer numItemCoda;
	private String numeroProtocollazione;
	private String numeroProtocolloDocChiusura;
	private String primaRigaDescrizione;
	private String progrEvento;
	private String quartaRigaDescrizione;
	private String secondaRigaDescrizione;
	private String statoAttualeProcedimento;
	private Integer termineNormato;
	private String terzaRigaDescrizione;
	private String tipoProtocollo;
	private String tipoProtocolloDocChiusura;
	private String tipoRecord;

}
