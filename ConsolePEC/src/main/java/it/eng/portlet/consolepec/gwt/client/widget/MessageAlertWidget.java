package it.eng.portlet.consolepec.gwt.client.widget;

import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * La widget visualizza messaggi ricevuti mediante {@link ShowMessageEvent} Durante gli eventi di attach e detach al DOM, la widget cancella il messaggio
 * 
 * @author pluttero
 * 
 */
public class MessageAlertWidget extends Composite {

	private static MessageAlertWidgetUiBinder uiBinder = GWT.create(MessageAlertWidgetUiBinder.class);

	interface MessageAlertWidgetUiBinder extends UiBinder<Widget, MessageAlertWidget> {
	}

	@UiField
	HTMLPanel alertPanel;

	/**
	 * Versione per ui:binder
	 */
	public MessageAlertWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	/**
	 * Versione per lavorare mediante evento {@link ShowMessageEvent}
	 * 
	 * @param eventBus
	 */
	@Inject
	public MessageAlertWidget(final EventBus eventBus) {
		this();
		eventBus.addHandler(ShowMessageEvent.getType(), new ShowMessageEvent.ShowMessageHandler() {

			@Override
			public void onShowMessage(ShowMessageEvent event) {
				if (event.getErrorMessage() != null) {
					showErrorMessage(event.getErrorMessage());
				} else if (event.getWarningMessage() != null) {
					showWarningMessage(event.getWarningMessage());
				} else if (event.getInfoMessage() != null) {
					showInfoMessage(event.getInfoMessage());
				} else if (event.isMessageDropped()) {
					dropMessage();
				}
			}
		});
	}

	public void showMessage(String message, String type) {
		String htmlSpan = type != null ? "<span>" + type + ": </span>" : "";
		String messHtml = htmlSpan + message;
		alertPanel.getElement().setInnerHTML(messHtml);
		alertPanel.setVisible(true);
		Window.scrollTo(0, 0);
	}

	public void showErrorMessage(String errorMessage) {
		alertPanel.setStyleName("alert-box error");
		showMessage(errorMessage, "Errore");
	}

	public void showWarningMessage(String warningMessage) {
		alertPanel.setStyleName("alert-box warning");
		showMessage(warningMessage, "Attenzione");
	}

	public void showInfoMessage(String infoMessage) {
		alertPanel.setStyleName("alert-box notice");
		showMessage(infoMessage, null);
	}

	public void reset() {
		dropMessage();
	}

	private void dropMessage() {
		alertPanel.setVisible(false);
		alertPanel.clear();

	}

	@Override
	protected void onLoad() {
		super.onLoad();
		dropMessage();
	}

	@Override
	protected void onUnload() {
		super.onUnload();
		dropMessage();
	}

}
