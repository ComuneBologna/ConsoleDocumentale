package it.eng.cobo.consolepec.commons.configurazioni;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true, of = "indirizzo")
@NoArgsConstructor
public class AnagraficaEmailOut extends TipologiaPratica implements Configurabile, Serializable {

	private static final long serialVersionUID = 4651812736202900105L;

	@NonNull
	private String indirizzo;

	private String server;

	private List<Azione> azioni = new ArrayList<>();
	private Date dataCreazione;
	private String usernameCreazione;
}
