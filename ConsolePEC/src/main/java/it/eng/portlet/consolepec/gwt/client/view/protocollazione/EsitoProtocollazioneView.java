package it.eng.portlet.consolepec.gwt.client.view.protocollazione;

import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.EsitoProtocollazionePresenter;
import it.eng.portlet.consolepec.gwt.client.widget.DownloadAllegatoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.YesNoRadioButton;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class EsitoProtocollazioneView extends ViewImpl implements EsitoProtocollazionePresenter.MyView {

	private final Widget widget;
	@UiField
	Button chiudiButton;
	@UiField
	Button confermaButton;
	@UiField
	HTMLPanel riepilogoPanel;
	@UiField
	Label titolo;
	@UiField(provided = true)
	YesNoRadioButton sceltaChiusura = new YesNoRadioButton("Chiudo un procedimento con questo PG ");
	@UiField(provided = true)
	YesNoRadioButton sceltaAvvio = new YesNoRadioButton("Avvia un procedimento con questo PG ");
	@UiField
	Button riversamentoCartaceoButton;
	@UiField
	DownloadAllegatoWidget downloadWidget;
	

	public interface Binder extends UiBinder<Widget, EsitoProtocollazioneView> {
	}

	@Inject
	public EsitoProtocollazioneView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public Button getChiudiButton() {
		return chiudiButton;
	}

	@Override
	public void setRiepilogo(String divsHTML) {
		riepilogoPanel.clear();
		riepilogoPanel.add(new HTMLPanel(divsHTML));
	}

	@Override
	public Button getConfermaButton() {
		return confermaButton;
	}
	
	@Override
	public YesNoRadioButton getSceltaChiusura(){
		return sceltaChiusura;
	}

	@Override
	public YesNoRadioButton getSceltaAvvio(){
		return sceltaAvvio;
	}
	
	@Override
	public void setRiversamentoCartaceoCommand(final Command command) {
		this.riversamentoCartaceoButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				command.execute();
			}
		});
	}

	@Override
	public DownloadAllegatoWidget getDownloadWidget() {
		return downloadWidget;
	}

}
