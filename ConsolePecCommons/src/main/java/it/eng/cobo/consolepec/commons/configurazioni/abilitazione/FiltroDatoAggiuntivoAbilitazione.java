package it.eng.cobo.consolepec.commons.configurazioni.abilitazione;

import java.util.Date;

import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = { "filtroDatoAggiuntivo", "tipoFascicolo" })
public class FiltroDatoAggiuntivoAbilitazione implements Abilitazione {

	private static final long serialVersionUID = -7450165620474758859L;

	@NonNull
	private DatoAggiuntivo filtroDatoAggiuntivo;
	@NonNull
	private String tipoFascicolo;

	public FiltroDatoAggiuntivoAbilitazione(DatoAggiuntivo filtroDatoAggiuntivo, String tipoFascicolo) {
		this.filtroDatoAggiuntivo = filtroDatoAggiuntivo;
		this.tipoFascicolo = tipoFascicolo;
	}

	private Date dataCreazione;
	private String usernameCreazione;
}
