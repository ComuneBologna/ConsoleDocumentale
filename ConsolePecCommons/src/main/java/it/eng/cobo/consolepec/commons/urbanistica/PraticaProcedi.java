package it.eng.cobo.consolepec.commons.urbanistica;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author GiacomoFM
 * @since 06/nov/2017
 */
@NoArgsConstructor
public class PraticaProcedi implements Serializable {

	private static final long serialVersionUID = -3506410196388929568L;

	@Getter @Setter private String chiaveAllegati;

	@Getter @Setter private String numeroProtocollo;
	@Getter @Setter private String annoProtocollo;

	@Getter @Setter private String ambito;
	@Getter @Setter private String oggetto;
	@Getter @Setter private String tipoPratica;

	@Getter @Setter private String indirizzoVia;
	@Getter @Setter private String indirizzoCivico;
	@Getter @Setter private String indirizzoEsponente;

	@Getter @Setter private String dataCreazione;

	@Getter private List<Nominativo> nominativi = new ArrayList<>();
	@Getter private List<AllegatoProcedi> allegati = new ArrayList<>();

	@Override
	public String toString() {
		return "PraticaProcedi [" + chiaveAllegati + "]: Numero/Anno Protocollo [" + numeroProtocollo + "/" + annoProtocollo + "], "
				+ nominativi.size() + " nominativi, " + allegati.size() + " allegati";
	}

}
