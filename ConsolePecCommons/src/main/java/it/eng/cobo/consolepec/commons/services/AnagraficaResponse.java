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
public final class AnagraficaResponse {

	@Getter 
	private List<Anagrafica> listaAnagrafiche = new ArrayList<Anagrafica>();

	@Getter 
	private Anagrafica anagrafica;
	
	public AnagraficaResponse(List<Anagrafica> listaAnagrafiche) {
		this.listaAnagrafiche = listaAnagrafiche;
	}

	public AnagraficaResponse(Anagrafica anagrafica) {
		this.anagrafica = anagrafica;
	}
}
