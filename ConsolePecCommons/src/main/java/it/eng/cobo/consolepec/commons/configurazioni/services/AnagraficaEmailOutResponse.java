package it.eng.cobo.consolepec.commons.configurazioni.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaEmailOut;
import lombok.Data;

@Data
public class AnagraficaEmailOutResponse implements Serializable {

	private static final long serialVersionUID = 8305497703779793446L;

	private AnagraficaEmailOut anagraficaEmailOut;

	private List<String> errors = new ArrayList<>();
	private boolean error = false;
}
