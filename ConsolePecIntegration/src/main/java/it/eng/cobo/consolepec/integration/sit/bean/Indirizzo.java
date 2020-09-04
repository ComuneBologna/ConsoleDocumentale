package it.eng.cobo.consolepec.integration.sit.bean;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Indirizzo {

	private String via, civico, esponente, quartiere, codiceQuartiere, cap;
	private int codiceVia;
	private boolean centro;

}
