package it.eng.cobo.consolepec.commons.configurazioni.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaIngresso;
import lombok.Data;

@Data
public class AnagraficaIngressoResponse implements Serializable {

	private static final long serialVersionUID = 1034006611931396863L;

	private AnagraficaIngresso anagraficaIngresso;

	private List<String> errors = new ArrayList<>();
	private boolean error = false;
}
