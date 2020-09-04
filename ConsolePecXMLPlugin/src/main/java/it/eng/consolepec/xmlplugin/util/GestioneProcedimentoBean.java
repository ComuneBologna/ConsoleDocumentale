package it.eng.consolepec.xmlplugin.util;

import it.eng.consolepec.xmlplugin.jaxb.PG;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

public class GestioneProcedimentoBean {
	
	@Getter
	@Setter
	String codUtente, modAvvioProcedimento, flagInterruzione, responsabile, provenienza, modalitaChiusura;
	@Getter
	@Setter
	PG pg, pgChiusura;
	@Getter
	@Setter
	Integer codTipologiaProcedimento, codUnitaOrgResponsabile, codUnitaOrgCompetenza, durata, codQuartiere,termine;
	@Getter
	@Setter
	Date dataInizioDecorrenzaProcedimento, dataChiusura;
	
}
