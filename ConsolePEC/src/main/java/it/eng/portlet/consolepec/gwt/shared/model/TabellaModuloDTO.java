package it.eng.portlet.consolepec.gwt.shared.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TabellaModuloDTO implements IsSerializable{
	private List<RigaDTO> righe = new ArrayList<RigaDTO>(); 
	
	
	public TabellaModuloDTO() {
		super();
	}

	public TabellaModuloDTO(List<RigaDTO> righe) {
		super();
		this.righe = righe;
	}
	
	public List<RigaDTO> getRighe() {
		return righe;
	}

	public static class RigaDTO implements IsSerializable{
		private List<ValoreModuloDTO> colonne = new ArrayList<ValoreModuloDTO>();

		
		public RigaDTO() {
			super();
		}

		public RigaDTO(List<ValoreModuloDTO> colonne) {
			super();
			this.colonne = colonne;
		}

		public List<ValoreModuloDTO> getColonne() {
			return colonne;
		}

		
	}
}
