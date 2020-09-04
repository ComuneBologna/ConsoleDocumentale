package it.eng.consolepec.spagicclient.bean.request.fascicolo;

import java.util.ArrayList;
import java.util.List;

import it.eng.consolepec.spagicclient.bean.request.datiaggiuntivi.DatoAggiuntivo;
import lombok.Getter;
import lombok.Setter;

public class FascicoloRequest {

	@Getter
	@Setter
	private String titolo, note, assegnatario, pathPraticaDaCollegare, numeroFattura, ragioneSociale, pIva, codicePIva, tipo;

	@Getter
	@Setter
	private List<DatoAggiuntivo> datiAggiuntivi = new ArrayList<>();
}
