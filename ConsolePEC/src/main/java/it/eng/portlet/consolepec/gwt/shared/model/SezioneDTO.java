package it.eng.portlet.consolepec.gwt.shared.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SezioneDTO implements NodoModulisticaDTO, IsSerializable {
	private String titolo;
	private List<NodoModulisticaDTO> nodi = new ArrayList<NodoModulisticaDTO>();
	
	public SezioneDTO() {
		super();
	}

	@Override
	public TipoNodoModulisticaDTO getTipoNodo() {
		return TipoNodoModulisticaDTO.SEZIONE;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public List<NodoModulisticaDTO> getNodi() {
		return nodi;
	}


}
