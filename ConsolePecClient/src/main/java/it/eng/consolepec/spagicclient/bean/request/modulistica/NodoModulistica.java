package it.eng.consolepec.spagicclient.bean.request.modulistica;

public interface NodoModulistica {
	TipoNodoModulistica getTipoNodo();
	
	
	
	public static enum TipoNodoModulistica {
		VALORE_MODULO,
		SEZIONE;
	}
}
