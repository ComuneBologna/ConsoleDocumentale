package it.eng.portlet.consolepec.gwt.client.view.protocollazione;

import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.ConfermaSceltaProtocollazionePresenter;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

public class ConfermaSceltaProtocollazioneView extends ViewImpl implements ConfermaSceltaProtocollazionePresenter.MyView {

	private final Widget widget;
	@UiField
	Button siButton;
	@UiField
	Button indietroButton;
	@UiField
	Button annullaButton;
	@UiField
	FocusWidget noButton;
	@UiField
	Label titolo;

	@UiField(provided = true)
	MessageAlertWidget messaggioAlertWidget;

	public interface Binder extends UiBinder<Widget, ConfermaSceltaProtocollazioneView> {
	}

	@Inject
	public ConfermaSceltaProtocollazioneView(final Binder binder, final EventBus eventBus) {
		messaggioAlertWidget = new MessageAlertWidget(eventBus);
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setConfermaProtocollazioneCommand(final Command command) {
		siButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				command.execute();

			}
		});

	}

	@Override
	public void setIndietroButtonCommand(final Command command) {
		indietroButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				command.execute();
			}
		});
	}

	@Override
	public void setAnnullaProtocollazioneCommand(final Command command) {
		annullaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				command.execute();

			}
		});

	}

	@Override
	public void setAbbandonaProtocollazioneCommand(final Command command) {
		noButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				command.execute();
			}
		});

	}

	@Override
	public void setTitle(String string) {
		titolo.setText(string);
	}
}
