package it.eng.consolepec.xmlplugin.factory;

import java.util.Date;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;

/**
 * Classe wrapper per modificare l'accesso a DatiPratica
 * 
 * @author pluttero
 * 
 */
public class DatiPraticaTaskAdapter {
	private DatiPratica dp;

	protected DatiPraticaTaskAdapter(DatiPratica dp) {
		this.dp = dp;
	}

	public void setFolderPath(String newFolderPath) {
		dp.setFolderPath(newFolderPath);
	}

	public void setTipo(TipologiaPratica tipo) {
		dp.setTipo(tipo);
	}

	public void setProvenienza(String provenienza) {
		dp.setProvenienza(provenienza);

	}

	public void setTitolo(String titolo) {
		dp.setTitolo(titolo);

	}

	public void setDataCreazione(Date dataCreazione) {
		dp.setDataCreazione(dataCreazione);

	}

	public void setIdDocumentale(String idDocumentale) {
		dp.setIdDocumentale(idDocumentale);
	}

	public void setVersione(int versione) {
		dp.set_version(versione);
	}

}
