package it.eng.consolepec.spagicclient.bean.request.modulistica;

import it.eng.consolepec.spagicclient.bean.request.Request;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class PraticaModulisticaRequest implements Request {

	@Getter
	@Setter
	private String titolo, assegnatario, destination;

	
	@Getter
	@Setter
	private String nome;
	
	@Getter
	private List<NodoModulistica> nodi = new ArrayList<NodoModulistica>();
}
