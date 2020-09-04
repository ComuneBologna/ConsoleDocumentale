package it.eng.portlet.consolepec.gwt.client.widget.protocollazione;

import it.eng.portlet.consolepec.gwt.shared.dto.Campo;
import it.eng.portlet.consolepec.gwt.shared.dto.Campo.TipoWidget;

import java.util.ArrayList;
import java.util.List;

public class IndirizzoWidgetDto extends AbstractGroupDto implements Comparable<IndirizzoWidgetDto> {

	private String descrizioneVia, civico, esponente, interno, esponenteInterno;

	public String getDescrizioneVia() {
		return descrizioneVia;
	}

	public void setDescrizioneVia(String descrizioneVia) {
		this.descrizioneVia = descrizioneVia;
	}

	public String getCivico() {
		return civico;
	}

	public void setCivico(String civico) {
		this.civico = civico;
	}

	public String getEsponente() {
		return esponente;
	}

	public void setEsponente(String esponente) {
		this.esponente = esponente;
	}

	public String getInterno() {
		return interno;
	}

	public void setInterno(String interno) {
		this.interno = interno;
	}

	public String getEsponenteInterno() {
		return esponenteInterno;
	}

	public void setEsponenteInterno(String esponenteInterno) {
		this.esponenteInterno = esponenteInterno;
	}

	@Override
	public int compareTo(IndirizzoWidgetDto o) {
		return toString().compareToIgnoreCase(o.toString());
	}

	@Override
	public String toString() {
		return (descrizioneVia + " " + civico).toUpperCase();
	}

	@Override
	public List<Campo> getCampo(int posizione) {
		List<Campo> listaCampi = new ArrayList<Campo>();

		listaCampi.add(new Campo("descrizione_via", descrizioneVia, true, true, false, "reci5#" + posizione, "Elenco indirizzi", 50, TipoWidget.TEXTBOX.name(), "descrizione via", 1, 100));
		listaCampi.add(new Campo("i5_codice_via", "", false, true, false, "reci5#" + posizione, "Elenco indirizzi", 5, TipoWidget.TEXTBOX.name(), "codice via", 2, 100));
		listaCampi.add(new Campo("i5_numero_civico", civico, true, true, false, "reci5#" + posizione, "Elenco indirizzi", 4, TipoWidget.TEXTBOX.name(), "numero civico", 3, 100));
		listaCampi.add(new Campo("i5_esponente_civico", esponente, true, true, false, "reci5#" + posizione, "Elenco indirizzi", 3, TipoWidget.TEXTBOX.name(), "esponente civico", 4, 100));
		listaCampi.add(new Campo("i5_numero_interno", interno, true, true, false, "reci5#" + posizione, "Elenco indirizzi", 3, TipoWidget.TEXTBOX.name(), "numero interno", 5, 100));
		listaCampi.add(new Campo("i5_esponente_interno", esponenteInterno, true, true, false, "reci5#" + posizione, "Elenco indirizzi", 1, TipoWidget.TEXTBOX.name(), "esponente interno", 6, 100));
		
		return listaCampi;
	}

}
