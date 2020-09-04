package it.eng.portlet.consolepec.gwt.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public interface NodoModulisticaDTO extends IsSerializable {
	
	TipoNodoModulisticaDTO getTipoNodo();
	
	public static enum TipoNodoModulisticaDTO implements IsSerializable {
		VALORE_MODULO,
		SEZIONE;
	}
}
