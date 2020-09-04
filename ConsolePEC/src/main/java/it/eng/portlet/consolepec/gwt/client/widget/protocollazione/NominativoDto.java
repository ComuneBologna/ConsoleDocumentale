package it.eng.portlet.consolepec.gwt.client.widget.protocollazione;

import it.eng.portlet.consolepec.gwt.shared.dto.Campo;
import it.eng.portlet.consolepec.gwt.shared.dto.Campo.TipoWidget;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Strings;


public class NominativoDto extends AbstractGroupDto implements Comparable<NominativoDto> {

	private String tipo, descrizione, codiceFiscale;

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	@Override
	public String toString() {
		if(Strings.isNullOrEmpty(codiceFiscale))
			return descrizione;
		return descrizione + " - " + codiceFiscale;
	}

	@Override
	public int compareTo(NominativoDto o) {
		return toString().compareTo(o.toString());
	}

	@Override
	public List<Campo> getCampo(int posizione) {
		List<Campo> listaCampi = new ArrayList<Campo>();

		listaCampi.add(new Campo("i2_tipo_nominativo", tipo, false, true, false, "reci2#" + posizione, "Elenco indirizzi", 1, TipoWidget.TEXTBOX.name(), "tipo nominativo", 1, 25));
		listaCampi.add(new Campo("i2_cf_piva_nominativo", codiceFiscale, true, true, false, "reci2#" + posizione, "Elenco indirizzi", 16, TipoWidget.TEXTBOX.name(), "codice fiscale", 2, 25));
		listaCampi.add(new Campo("i2_nominativo", descrizione, true, true, false, "reci2#" + posizione, "Elenco indirizzi", 60, TipoWidget.TEXTBOX.name(), "nominativo", 3, 25));

		return listaCampi;
	}
}
