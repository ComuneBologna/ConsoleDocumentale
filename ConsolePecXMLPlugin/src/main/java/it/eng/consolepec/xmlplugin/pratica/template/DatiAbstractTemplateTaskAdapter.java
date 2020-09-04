package it.eng.consolepec.xmlplugin.pratica.template;

import it.eng.consolepec.xmlplugin.factory.DatiPraticaTaskAdapter;

/**
 * Classe wrapper per modificare l'accesso a DatiTemplateGenerico
 * 
 * 
 */
public class DatiAbstractTemplateTaskAdapter extends DatiPraticaTaskAdapter {

	private DatiAbstractTemplate datiAbstractTemplate;

	protected DatiAbstractTemplateTaskAdapter(DatiAbstractTemplate dp) {
		super(dp);
		this.datiAbstractTemplate = dp;
	}

}
