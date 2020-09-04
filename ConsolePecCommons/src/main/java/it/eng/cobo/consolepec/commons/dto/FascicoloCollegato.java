package it.eng.cobo.consolepec.commons.dto;

import java.util.Date;

import lombok.Data;

@Data
public class FascicoloCollegato {

	private String idDocumentale;
	private Ruolo ruolo;
	private Date dataCollegamento;
}
