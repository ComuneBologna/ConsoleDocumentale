package it.eng.consolepec.spagicclient.bean.request.template;

import it.eng.consolepec.spagicclient.bean.request.Request;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public abstract class AbstractCreaTemplate implements Request {

	@Getter
	@Setter
	private String assegnatario, destination;

	@Getter
	@Setter
	String nome;
	@Getter
	@Setter
	String descrizione;
	@Getter
	@Setter
	List<CampoTemplate> campi = new ArrayList<CampoTemplate>();
	@Getter
	@Setter
	List<String> fascicoliAbilitati = new ArrayList<String>();
}
