package it.eng.cobo.consolepec.commons.dto;

import java.util.ArrayList;
import java.util.List;

import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import lombok.Data;

@Data
public class UploadAllegatoInput {

	private List<DatoAggiuntivo> datiAggiuntivi = new ArrayList<>();
	private String nome;
	private List<String> tag = new ArrayList<>();

}
