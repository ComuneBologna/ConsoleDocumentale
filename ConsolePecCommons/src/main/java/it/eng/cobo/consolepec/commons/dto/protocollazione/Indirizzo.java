package it.eng.cobo.consolepec.commons.dto.protocollazione;

import lombok.Data;

@Data
public class Indirizzo {
	private String descrizioneVia;
	private String codiceVia;
	private String numeroCivico;
	private String esponenteCivico;
	private String numeroInterno;
	private String esponenteInterno;
}
