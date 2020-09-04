package it.eng.consolepec.xmlplugin.util;

import java.util.ArrayList;
import java.util.List;

import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class DatiModificaFascicolo {

	@Getter
	@Setter
	private String idDocumentaleFascicolo;

	@Getter
	@Setter
	private String titolo;

	@Getter
	@Setter
	private String tipologiaFascicolo;

	@Getter
	@Setter
	private List<DatoAggiuntivo> datoAggiuntivo = new ArrayList<>();

	@Override
	public String toString() {
		return "Dati Modifica Fascicolo id documentale: " + getIdDocumentaleFascicolo() + " [titolo: " + getTitolo() + ", tipo: " + getTipologiaFascicolo() + "]";
	}

}
