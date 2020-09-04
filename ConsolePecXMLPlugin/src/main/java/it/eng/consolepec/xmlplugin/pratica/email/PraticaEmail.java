package it.eng.consolepec.xmlplugin.pratica.email;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.consolepec.xmlplugin.factory.MetadatiPratica;
import it.eng.consolepec.xmlplugin.factory.Pratica;

import java.util.Map;

public interface PraticaEmail extends Pratica<DatiEmail> {

	public Map<MetadatiPratica, Object> getMetadata();

	public boolean isMailCompleta();
	
	public boolean isEmailInteroperabile();
	
	public boolean isRiattivabileElettorale();

	public TipologiaPratica getTipo();
	
	@Override
	public DatiEmail getDati();
}
