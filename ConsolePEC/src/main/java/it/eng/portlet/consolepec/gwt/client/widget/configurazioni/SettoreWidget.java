package it.eng.portlet.consolepec.gwt.client.widget.configurazioni;

import it.eng.cobo.consolepec.commons.profilazione.Settore;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.SpacebarSuggestOracle;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

/**
 * 
 * @author biagiot
 *
 */
public class SettoreWidget extends ComplexPanel {

	private DivElement divSingle;
	private DivElement divContainer;
	private DivElement divElementi;
	private SuggestBox settoriSuggestBox;
	private Button button;
	private HandlerRegistration handlerRegistration;
		
	public SettoreWidget() {
		super();
		divSingle = Document.get().createDivElement();
		divSingle.setClassName("single");
		setElement(divSingle);

		divContainer = Document.get().createDivElement();
		divContainer.setClassName("abstract");
		divSingle.appendChild(divContainer);
		
		settoriSuggestBox = new SuggestBox(new SpacebarSuggestOracle(new ArrayList<String>()));
		button = new Button("Aggiungi");
		button.setStyleName("btn");
		
		add(settoriSuggestBox, divContainer);
		add(button, divContainer);
		
		divElementi = Document.get().createDivElement();
		divContainer.appendChild(divElementi);
	}
	
	private Settore settoreSelezionato;
	
	public Settore getSettore() {
		return settoreSelezionato;
	}
	
	public void render(final ConfigurazioniHandler configurazioniHandler, Settore settore) {
		
		if (handlerRegistration != null) {
			handlerRegistration.removeHandler();
		}
		
		List<String> settori = new ArrayList<String>();
		for (Settore s : configurazioniHandler.getSettori()) {
			settori.add(s.getNome());
		}
		
		SpacebarSuggestOracle so = (SpacebarSuggestOracle) settoriSuggestBox.getSuggestOracle();
		so.clear();
		so.setSuggestions(settori);
		
		handlerRegistration = button.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				String selectedValue = settoriSuggestBox.getValue();
				Settore s = configurazioniHandler.getSettore(selectedValue);
				if (s != null) {
					popolaSettore(s);
				} 
			}
		});
		
		
		settoriSuggestBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			
			@Override
			public void onSelection(SelectionEvent<Suggestion> arg0) {
				String selectedValue = settoriSuggestBox.getValue();
				Settore s = configurazioniHandler.getSettore(selectedValue);
				
				if (s != null) {
					onSelect(s);
					
				} else {
					onDeselect();
				}
			}
		});
		
		if (settore != null) {
			popolaSettore(settore);
			
		} else {
			onDeselect();
		}
	}
	
	private void onSelect(final Settore settore) {		
		divElementi.removeAllChildren();
		settoriSuggestBox.setValue(settore.getNome());
		button.setEnabled(true);
	}
	
	private void onDeselect() {
		divElementi.removeAllChildren();
		this.settoreSelezionato = null;
		settoriSuggestBox.setValue(null);
		button.setEnabled(false);
	}
	
	private void popolaSettore(Settore settore) {
		divElementi.removeAllChildren();
		
		HeadingElement h6S = Document.get().createHElement(6);
		h6S.setInnerText("Settore:");
		SpanElement spanElementS = Document.get().createSpanElement();
		spanElementS.setClassName("label");
		spanElementS.setInnerText(settore.getNome());
		divElementi.appendChild(h6S);
		divElementi.appendChild(spanElementS);
		
		Button elimina = new Button("Elimina");
		elimina.setStyleName("btn black");
		elimina.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				onDeselect();
			}
		});
		add(elimina, divElementi);
		
		if (settore.getSettoriSubordinati() != null && !settore.getSettoriSubordinati().isEmpty()) {
			
			HeadingElement h6 = Document.get().createHElement(6);
			h6.setInnerText("Settori subordinati:");
			divElementi.appendChild(h6);
			
			for (String settoreSubordinato : settore.getSettoriSubordinati()) {
				SpanElement spanElement = Document.get().createSpanElement();
				spanElement.setClassName("label");
				spanElement.setInnerText(settoreSubordinato);
				divElementi.appendChild(spanElement);
			}
		}
		
		this.settoreSelezionato = settore;
	}
}
