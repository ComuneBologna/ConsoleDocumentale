package it.eng.portlet.consolepec.gwt.shared.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ModuloDTO implements IsSerializable {
	
	protected String nome;
	protected List<ValoreModuloDTO> valori = new ArrayList<ValoreModuloDTO>(); 
	
	public ModuloDTO() {
		
	}

	public ModuloDTO(String nome) {
		super();
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public List<ValoreModuloDTO> getValori() {
		return valori;
	}
	

}
