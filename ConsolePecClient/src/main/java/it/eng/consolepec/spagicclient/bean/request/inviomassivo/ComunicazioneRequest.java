package it.eng.consolepec.spagicclient.bean.request.inviomassivo;

import it.eng.consolepec.spagicclient.bean.request.Request;
import lombok.Getter;
import lombok.Setter;

public class ComunicazioneRequest implements Request {

	@Getter
	@Setter
	String assegnatario;
	
	@Getter
	@Setter
	String codice;
	@Getter
	@Setter
	String descrizione;
	@Getter
	@Setter
	String idTemplate;
		
}
