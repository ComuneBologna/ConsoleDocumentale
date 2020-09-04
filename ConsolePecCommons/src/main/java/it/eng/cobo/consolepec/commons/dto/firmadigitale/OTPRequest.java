package it.eng.cobo.consolepec.commons.dto.firmadigitale;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OTPRequest {

	private String user;
	private String password;
	private String tipoCredenziali;

}
