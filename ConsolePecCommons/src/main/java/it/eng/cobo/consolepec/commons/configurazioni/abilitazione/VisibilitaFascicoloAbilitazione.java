package it.eng.cobo.consolepec.commons.configurazioni.abilitazione;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true, of = {})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VisibilitaFascicoloAbilitazione extends VisibilitaPraticaAbilitazione {
	private static final long serialVersionUID = -8517724661038592196L;

	public VisibilitaFascicoloAbilitazione(String tipo) {
		super(tipo);
	}
}
