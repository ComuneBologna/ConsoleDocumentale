package it.eng.cobo.consolepec.commons.dto.firmadigitale;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FirmaRequest {

	private List<String> nomiAllegati;
	private String userFirma;
	private String passFirma;
	private String otp;
	private String tipoFirma;

}
