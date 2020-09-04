package it.eng.portlet.consolepec.gwt.client.view.fascicolo;

import it.eng.portlet.consolepec.gwt.client.command.BackToPraticaCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.ConfermaMailDaTemplatePresenter;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

public class ConfermaMailDaTemplateView extends ViewImpl implements ConfermaMailDaTemplatePresenter.MyView {

	private final Widget widget;
	
	@UiField(provided = true)
	MessageAlertWidget messageAlertWidget;
	
	@UiField
	Button confermaButton;
	@UiField
	Button annullaButton;

	public interface Binder extends UiBinder<Widget, ConfermaMailDaTemplateView> {
	}

	@Inject
	public ConfermaMailDaTemplateView(final Binder binder, final EventBus eventBus) {
		messageAlertWidget = new MessageAlertWidget(eventBus);
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setAnnullaCommand(final BackToPraticaCommand backToFascicoloCommand) {
		this.annullaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				backToFascicoloCommand.execute();
			}
		});
	}

	@Override
	public void setConfermaCommand(final Command nuovaMailDaTemplateCommand) {
		this.confermaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				nuovaMailDaTemplateCommand.execute();
			}
		});
	}
}
