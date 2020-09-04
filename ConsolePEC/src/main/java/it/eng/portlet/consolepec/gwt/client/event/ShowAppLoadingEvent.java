package it.eng.portlet.consolepec.gwt.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.ui.HTML;

public class ShowAppLoadingEvent extends GwtEvent<ShowAppLoadingEvent.ShowAppLoadingHandler> {

	public static Type<ShowAppLoadingHandler> TYPE = new Type<ShowAppLoadingHandler>();
	private final Boolean complete;
	private int delayMs = 0;
	private HTML htmlMessage;

	public interface ShowAppLoadingHandler extends EventHandler {
		void onAppLoading(ShowAppLoadingEvent event);
	}

	public ShowAppLoadingEvent(Boolean uncomplete) {
		this.complete = !uncomplete;
		this.delayMs = 0;
	}

	public ShowAppLoadingEvent(Boolean uncomplete, int millis) {
		this.complete = !uncomplete;
		this.delayMs = millis;
	}

	public Boolean isComplete() {
		return complete;
	}

	@Override
	protected void dispatch(ShowAppLoadingHandler handler) {
		handler.onAppLoading(this);
	}

	@Override
	public Type<ShowAppLoadingHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ShowAppLoadingHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Boolean uncomplete) {
		ShowAppLoadingEvent showAppLoadingEvent = new ShowAppLoadingEvent(uncomplete);
		source.fireEvent(showAppLoadingEvent);
	}

	public static void fire(HasHandlers source, Boolean uncomplete, int timer) {
		ShowAppLoadingEvent showAppLoadingEvent = new ShowAppLoadingEvent(uncomplete, timer);
		source.fireEvent(showAppLoadingEvent);
	}

	public int showMessageAlfterTimeOut() {
		return delayMs;
	}

	public HTML getHtmlMessage() {
		return htmlMessage;
	}

}
