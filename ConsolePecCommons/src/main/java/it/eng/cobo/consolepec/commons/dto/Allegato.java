package it.eng.cobo.consolepec.commons.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.eng.cobo.consolepec.commons.firmadigitale.Firmatario.TipoFirma;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import lombok.Data;

@Data
public class Allegato {

	private String nome;
	private String versione;
	private Long dimensioneByte;
	private List<DatoAggiuntivo> datiAggiuntivi = new ArrayList<>();
	private List<String> tag = new ArrayList<>();
	private String hash;
	private Date dataCaricamento;
	private String oggetto;
	private TipoFirma tipoFirma;
	private boolean firmato;
}
