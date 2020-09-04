package it.eng.portlet.consolepec.gwt.client.widget.datiaggiuntivi;

import it.eng.portlet.consolepec.gwt.client.widget.datiaggiuntivi.FormDatiAggiuntiviWidget.TipoPagina;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.SimpleInputListWidget;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.SpacebarSuggestOracle;

import java.util.List;

import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class DatoAggiuntivoMultiploWidget extends SimpleInputListWidget{
	
	private SuggestOracle suggestOracle;
	
	public DatoAggiuntivoMultiploWidget(List <String> elenco, TipoPagina modalita){
		super(elenco, getListStyle(modalita));
	
	}
	
	@Override
	protected Widget getWidgetInserimento(final TextBox itemBox, List<String> elenco) {
		suggestOracle = new SpacebarSuggestOracle(elenco);
		final SuggestBox suggestBox = new SuggestBox(suggestOracle, itemBox);
		suggestBox.setStyleName("testo");
		suggestBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				deselectItem(suggestBox, list);	
				itemBox.setFocus(true);
				setRequired(false);
			}
		});
		return suggestBox; 
	}

	public void setPlaceholder(String placeholder) {
		InputElement inputElement = itemBox.getElement().cast();
		inputElement.setAttribute("placeholder", placeholder);
	}

	private static String getListStyle(TipoPagina modalita) {
		if(modalita.equals(TipoPagina.RICERCA)){
			return "multitesto";	// stile per ricerche
		} else {
			return "token-input-list-facebook"; // stile standard del widget per inserimento e modifica
		}
	}
	
	
}
