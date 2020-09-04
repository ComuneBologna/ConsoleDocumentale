package it.eng.cobo.consolepec.commons.configurazioni.abilitazione;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(of = {}, callSuper = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LetturaIngressoAbilitazione extends IngressoAbilitazione {
	private static final long serialVersionUID = 7280948992266636405L;

	private boolean primoAssegnatario;

	public LetturaIngressoAbilitazione(String tipoIngresso, String indirizzo) {
		super(tipoIngresso, indirizzo);
	}
}
