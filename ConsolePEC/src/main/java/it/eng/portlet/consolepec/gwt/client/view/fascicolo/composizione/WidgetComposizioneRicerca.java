package it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;

import it.eng.portlet.consolepec.gwt.client.composizione.RicercaComposizioneFascicoloHandler;
import lombok.Getter;

/**
 * @author GiacomoFM
 * @since 13/mar/2019
 */
public class WidgetComposizioneRicerca extends Composite {

	@Getter
	private RicercaComposizioneFascicoloHandler ricercaHandler;

	@UiField(provided = true)
	SuggestBox ricerca;
	@UiField
	Button ricercaBtn;
	@UiField
	Button resetBtn;

	private static Binder binder = GWT.create(Binder.class);

	public interface Binder extends UiBinder<Widget, WidgetComposizioneRicerca> {/**/}

	public WidgetComposizioneRicerca(final RicercaComposizioneFascicoloHandler ricercaHandler) {
		this.ricercaHandler = ricercaHandler;
		ricerca = new SuggestBox(new RicercaComposizioneFascicoloSuggestOracle(ricercaHandler));
		initWidget(binder.createAndBindUi(this));
		ricerca.getValueBox().getElement().setAttribute("placeholder", "Ricerca");

		ricerca.addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
					ricercaBtn.click();
				}
			}
		});
	}

	public void addRicercaClickHandler(final ClickHandler clickHandler) {
		ricercaBtn.addClickHandler(clickHandler);
	}

	public void addResetClickHandler(final ClickHandler clickHandler) {
		resetBtn.addClickHandler(clickHandler);
	}

	public String getRicercaText() {
		return ricerca == null || ricerca.getText() == null || ricerca.getText().trim().isEmpty() ? null : ricerca.getText();
	}

	public void setRicercaText(String text) {
		this.ricerca.setText(text);
	}

	public void ricerca() {
		ricercaBtn.click();
	}

	public void reset() {
		resetBtn.click();
	}

}
