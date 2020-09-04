package it.eng.cobo.consolepec.commons.procedimento;

import java.io.Serializable;

import lombok.Data;

@Data
public class ChiusuraProcedimentoOutput implements Serializable {

	private static final long serialVersionUID = -5424472489836090919L;

	private String codice;
	private String descrizione;
	private int durata;

}
