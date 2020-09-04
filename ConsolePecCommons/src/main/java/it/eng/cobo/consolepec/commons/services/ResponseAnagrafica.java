package it.eng.cobo.consolepec.commons.services;

import it.eng.cobo.consolepec.commons.rubrica.Anagrafica;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author GiacomoFM
 * @since 25/ott/2017
 */
@NoArgsConstructor
public final class ResponseAnagrafica {

	@Getter private List<Anagrafica> listaAnagrafiche = new ArrayList<Anagrafica>();

	@Getter private Anagrafica anagrafica;

	@Getter private boolean error = false;

	@Getter private String errorMessage;

	public ResponseAnagrafica(List<Anagrafica> listaAnagrafiche) {
		this.listaAnagrafiche = listaAnagrafiche;
	}

	public ResponseAnagrafica(Anagrafica anagrafica) {
		this.anagrafica = anagrafica;
	}

	public ResponseAnagrafica(String errorMessage) {
		this.errorMessage = errorMessage;
		this.error = true;
	}
}
