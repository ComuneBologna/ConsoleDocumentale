package it.eng.portlet.consolepec.gwt.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;

public class ShowMessageEvent extends GwtEvent<ShowMessageEvent.ShowMessageHandler> {

	public static Type<ShowMessageHandler> TYPE = new Type<ShowMessageHandler>();

	private String errorMessage;
	private String warningMessage;
	private String infoMessage;
	private boolean dropMessage;

	public String getInfoMessage() {
		return infoMessage;
	}

	public void setInfoMessage(String infoMessage) {
		this.infoMessage = infoMessage;
	}

	public boolean isMessageDropped() {
		return dropMessage;
	}

	public void setMessageDropped(boolean dropMessage) {
		this.dropMessage = dropMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	private void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	private void setErrorMessage(Throwable caught) {
		setErrorMessage(ConsolePecConstants.ERROR_MESSAGE, caught);
	}

	public void setErrorMessage(String errorMessage, Throwable caught) {
		setErrorMessage(errorMessage);
		if (caught != null) {
			// TODO gestione log error
		}
	}

	public String getWarningMessage() {
		return warningMessage;
	}

	public void setWarningMessage(String warningMessage) {
		this.warningMessage = warningMessage;
	}

	public interface ShowMessageHandler extends EventHandler {
		void onShowMessage(ShowMessageEvent event);
	}

	public ShowMessageEvent() {}

	@Override
	protected void dispatch(ShowMessageHandler handler) {
		handler.onShowMessage(this);
	}

	@Override
	public Type<ShowMessageHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ShowMessageHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new ShowMessageEvent());
	}

	public void setErrorMessage(Object t) {
		if (t instanceof String) {
			setErrorMessage((String) t);
		} else if (t instanceof Throwable) {
			setErrorMessage((Throwable) t);
		}
	}
}
