package it.eng.consolepec.xmlplugin.pratica.fascicolo;

import it.eng.consolepec.xmlplugin.factory.DatiPraticaTaskAdapter;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Stato;

public class DatiFascicoloTaskAdapter extends DatiPraticaTaskAdapter {

	private DatiFascicolo datiFascicolo;

	protected DatiFascicoloTaskAdapter(DatiFascicolo dp) {
		super(dp);
		this.datiFascicolo = dp;
	}
	
	public void setStato(Stato nuovo){
		datiFascicolo.setStato(nuovo);
	}
	
}
