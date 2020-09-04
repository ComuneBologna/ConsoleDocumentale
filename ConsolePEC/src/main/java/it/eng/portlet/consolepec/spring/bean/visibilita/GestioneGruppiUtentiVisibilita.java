package it.eng.portlet.consolepec.spring.bean.visibilita;

import java.util.TreeSet;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.exception.ConsoleDocumentaleException;
import it.eng.portlet.consolepec.gwt.shared.model.GruppoVisibilita;

public interface GestioneGruppiUtentiVisibilita {

	public TreeSet<GruppoVisibilita> getUtentiGruppiVisibilita(TipologiaPratica tipoPratica) throws ConsoleDocumentaleException;

}
