package it.eng.portlet.consolepec.gwt.client.widget.formricerca;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;

import java.util.List;
import java.util.Map;

import lombok.Getter;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractGruppoFiltriWidget extends Composite{
	
	protected KeyDownHandler invioBt;
	protected DisclosurePanel disclosurePanel;
	
	@Getter
	protected ProfilazioneUtenteHandler profilazioneUtenteHandler;
	
	@Getter
	protected ConfigurazioniHandler configurazioniHandler;
	
	public abstract void serializza(CercaPratiche dto);
	
	public abstract void reset();
	
	public abstract List<TipologiaPratica> getTipiPraticheGestite();
	
	public abstract String getDescrizione();
	
	
	protected void setPlaceholder(Element element, String placeholder) {
		InputElement inputElement = element.cast();
		inputElement.setAttribute("placeholder", placeholder);
	}
	
	protected void configura(final Command cercaCommand){
		invioBt = new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					cercaCommand.execute();
				}
			}
		};
	}
	
	
	public void setDisclosurePanel(DisclosurePanel disclosurePanel) {
		this.disclosurePanel = disclosurePanel;
	}
	
	protected void hideParentPanel(){
		if(disclosurePanel != null){
			disclosurePanel.setVisible(false);
		}
	}
	
	protected void showParentPanel(){
		if(disclosurePanel != null){
			disclosurePanel.setVisible(true);
		}
	}
	
	protected void configuraCampo(Widget w, String placeholder) {
		w.addDomHandler(invioBt, KeyDownEvent.getType());
		InputElement inputElement = w.getElement().cast();
		inputElement.setAttribute("placeholder", placeholder);
	}

	protected void onTipoPraticaSelection(TipologiaPratica tp) {
		
	}
	
	public void setParametriFissiWorklist(Map<String, Object> parametriFissiWorklist) {

	}
	
	public boolean hasFiltriPersonalizzati() {
		return false;
	}
}
