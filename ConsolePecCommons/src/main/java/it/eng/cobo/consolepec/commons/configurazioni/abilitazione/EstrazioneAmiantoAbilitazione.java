package it.eng.cobo.consolepec.commons.configurazioni.abilitazione;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EstrazioneAmiantoAbilitazione implements Abilitazione {

	private static final long serialVersionUID = 8519580886155812441L;
	
	private Date dataCreazione;
	private String usernameCreazione;
}
