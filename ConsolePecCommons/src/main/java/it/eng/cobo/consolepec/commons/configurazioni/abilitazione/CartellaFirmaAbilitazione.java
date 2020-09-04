package it.eng.cobo.consolepec.commons.configurazioni.abilitazione;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "riassegnabilePerRuolo")
public class CartellaFirmaAbilitazione implements Abilitazione {

	private static final long serialVersionUID = 935429159905823035L;

	protected Date dataCreazione;
	protected String usernameCreazione;

	private boolean riassegnabilePerRuolo;

}
