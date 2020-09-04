package it.eng.consolepec.xmlplugin.pratica.email;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;



public class XMLPraticaEmailDaEprotocollo extends XMLPraticaEmailIn implements PraticaEmailDaEprotocollo {
	
	public XMLPraticaEmailDaEprotocollo() {
		// richiesto da reflection
	}
	
	@Override
	public TipologiaPratica getTipo() {
		return TipologiaPratica.EMAIL_EPROTOCOLLO;
	}
}
