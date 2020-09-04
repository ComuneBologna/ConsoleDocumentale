package it.eng.cobo.consolepec.commons.dto;

import java.util.ArrayList;
import java.util.List;

import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import lombok.Data;

@Data
public class CreateFascicoloInput {

	private String tipo;
	private String titolo;
	private String note;
	private List<DatoAggiuntivo> datiAggiuntivi = new ArrayList<>();
	private String ruoloAssegnatario;
}
