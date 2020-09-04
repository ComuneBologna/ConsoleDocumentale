package it.eng.cobo.consolepec.commons.configurazioni;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true, of = {})
@NoArgsConstructor
public class AnagraficaModello extends TipologiaPratica implements Configurabile {
	private static final long serialVersionUID = -3177772123229985571L;

	private List<Azione> azioni = new ArrayList<>();
	private Date dataCreazione;
	private String usernameCreazione;

}
