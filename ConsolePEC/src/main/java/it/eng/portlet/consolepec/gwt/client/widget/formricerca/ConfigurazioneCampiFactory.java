package it.eng.portlet.consolepec.gwt.client.widget.formricerca;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.portlet.consolepec.gwt.client.widget.formricerca.FormRicercaBase.ConfigurazioneCampiPerTipo;

public class ConfigurazioneCampiFactory {

	public static ConfigurazioneCampiPerTipo getConfigurazione(TipologiaPratica tipologiaPratica) {
		
		ConfigurazioneCampiPerTipo cctp = new ConfigurazioneCampiPerTipo(tipologiaPratica);

		if (TipologiaPratica.EMAIL_IN.equals(tipologiaPratica)) {
			cctp.setTitoloDataA("Data di arrivo A");
			cctp.setTitoloDataDa("Data di arrivo Da");
			cctp.setTitoloProvenienza("Mittente");
			cctp.setTitoloTitolo("Oggetto");
			
		} else if (TipologiaPratica.EMAIL_OUT.equals(tipologiaPratica)) {
			cctp.setTitoloDataA("Data di invio A");
			cctp.setTitoloDataDa("Data di invio Da");
			cctp.setTitoloProvenienza("Mittente");
			cctp.setTitoloTitolo("Oggetto");
		}
		
		return cctp;
	}
}
