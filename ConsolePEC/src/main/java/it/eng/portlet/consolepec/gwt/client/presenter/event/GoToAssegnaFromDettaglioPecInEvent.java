package it.eng.portlet.consolepec.gwt.client.presenter.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class GoToAssegnaFromDettaglioPecInEvent extends GwtEvent<GoToAssegnaFromDettaglioPecInEvent.GoToAssegnaFromDettaglioPecInHandler> {

	public static Type<GoToAssegnaFromDettaglioPecInHandler> TYPE = new Type<GoToAssegnaFromDettaglioPecInHandler>();

	public interface GoToAssegnaFromDettaglioPecInHandler extends EventHandler {
		void onGoToAssegnaFromDettaglioPecIn(GoToAssegnaFromDettaglioPecInEvent event);
	}

	private String pecInId;

	public GoToAssegnaFromDettaglioPecInEvent(String pecInId) {
		this.pecInId = pecInId;
	}

	@Override
	protected void dispatch(GoToAssegnaFromDettaglioPecInHandler handler) {
		handler.onGoToAssegnaFromDettaglioPecIn(this);
	}

	@Override
	public Type<GoToAssegnaFromDettaglioPecInHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<GoToAssegnaFromDettaglioPecInHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String pecInId) {
		source.fireEvent(new GoToAssegnaFromDettaglioPecInEvent(pecInId));
	}

	public String getPecInId() {
		return pecInId;
	}
}
