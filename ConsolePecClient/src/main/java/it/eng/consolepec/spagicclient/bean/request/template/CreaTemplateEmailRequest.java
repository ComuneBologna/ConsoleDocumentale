package it.eng.consolepec.spagicclient.bean.request.template;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class CreaTemplateEmailRequest extends AbstractCreaTemplate {

	
	@Getter
	@Setter
	String oggettoMail;
	@Getter
	@Setter
	String corpoMail;
	@Getter
	@Setter
	String mittente;
	@Getter
	@Setter
	List<String> destinatari = new ArrayList<String>();
	@Getter
	@Setter
	List<String> destinatariCC = new ArrayList<String>();
}
