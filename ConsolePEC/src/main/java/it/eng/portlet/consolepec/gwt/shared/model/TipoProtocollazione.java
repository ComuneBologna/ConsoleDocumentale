package it.eng.portlet.consolepec.gwt.shared.model;

import it.eng.consolepec.xmlplugin.factory.DatiPratica;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Versione per GWT di {@link DatiPratica.TipoProtocollazione}
 * @author pluttero
 *
 */
public enum TipoProtocollazione implements IsSerializable {
	ENTRATA("Entrata", "E"), USCITA("Uscita","U"), INTERNA("Interna","I");
	
	private String label, codice;

	TipoProtocollazione(String label, String codice){
		this.label = label;
		this.codice = codice;
	}

	public String getLabel() {
		return label;
	}
	
	public String getCodice(){
		return codice;
	}
	public static TipoProtocollazione fromLabel(String label) {
		for (TipoProtocollazione dto : values()) {
			if (dto.getLabel().equals(label))
				return dto;
		}
		return null;
	}

}
