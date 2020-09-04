package it.eng.cobo.consolepec.commons.configurazioni.abilitazione;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true, of = {})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VisibilitaModelloAbilitazione extends VisibilitaPraticaAbilitazione {
	private static final long serialVersionUID = 1776231630451492789L;

	public VisibilitaModelloAbilitazione(String tipo) {
		super(tipo);
	}
}
