package it.eng.portlet.consolepec.gwt.client.widget.datiaggiuntivi;

import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.portlet.consolepec.gwt.client.widget.datiaggiuntivi.FormDatiAggiuntiviWidget.TipoPagina;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.ListBox;

public class DatiAggiuntiviListaBox extends ListBox {

	private List<String> opzioni = new ArrayList<String>();

	public static final String EMPTY = "";

	private TipoPagina tipo;

	public DatiAggiuntiviListaBox(DatoAggiuntivo dato, TipoPagina tipo) {
		super();
		this.tipo = tipo;
		if(TipoPagina.RICERCA.equals(tipo)) {	// traduzione: per la ricerca aggiungo sempre l'opzione vuota...
			this.opzioni.add(dato.getDescrizione());
			this.opzioni.add(EMPTY);
			
		} else	{						// ...negli altri casi solo se il dato NON è obbligatorio
			if(!DatiAggiuntiviWidgetUtil.isObbligatorio(dato))
				this.opzioni.add(EMPTY);
		}
		
		this.opzioni.addAll(DatiAggiuntiviWidgetUtil.getValoriPredefiniti(dato));
		
		int count = 0;
		for (String value : opzioni) {
			
			if(TipoPagina.RICERCA.equals(tipo) && count == 0) {
				addItem(value, "");
				
			} else {
				addItem(value);
			}
			
			count++;
		}
		
		if(TipoPagina.RICERCA.equals(tipo)) {
			getElement().getElementsByTagName("option").getItem(0).setAttribute("disabled", "disabled");
		}
	}

	/*
	 * imposta il valore visualizzato nella listbox a partire dal valore del dato aggiuntivo
	 */
	public void setValue(String valore) {
		if (valore == null) {
			setItemSelected(0, true);
		} else {
			int index = opzioni.indexOf(valore);
			
			if (index >= 0)
				setItemSelected(index, true); // mantenuto per retrocompatibilità
		}
	}

	public String getValue() {
		String value = opzioni.get(getSelectedIndex());
		if(tipo.equals(TipoPagina.RICERCA) && EMPTY.equals(value)){
			return null;
		}
		
		if(tipo.equals(TipoPagina.RICERCA) && getSelectedIndex() == 0) {
			return null;
		}
		
		return value;
	}

	public void setEditable(boolean enabled) {
		setEnabled(enabled);
		if (enabled) {
			removeStyleName("testo disabilitato");
			setStyleName("testo");
		} else {
			removeStyleName("testo");
			setStyleName("testo disabilitato");
		}
	}
}
