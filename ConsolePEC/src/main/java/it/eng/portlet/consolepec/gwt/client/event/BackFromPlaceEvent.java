package it.eng.portlet.consolepec.gwt.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

import lombok.Getter;

public class BackFromPlaceEvent extends GwtEvent<BackFromPlaceEvent.BackFromPlaceHandler> {

	public static Type<BackFromPlaceHandler> TYPE = new Type<BackFromPlaceHandler>();
	private String idPratica;

	@Getter
	private boolean reload;

	public BackFromPlaceEvent() {}

	public BackFromPlaceEvent(String idPratica) {
		this.idPratica = idPratica;
	}

	public BackFromPlaceEvent(boolean reload) {
		this.reload = reload;
	}

	public BackFromPlaceEvent(String idPratica, boolean reload) {
		this.idPratica = idPratica;
		this.reload = reload;
	}

	public interface BackFromPlaceHandler extends EventHandler {
		void onBackFromPlace(BackFromPlaceEvent event);
	}

	public interface BackFromTokenHasHandlers extends HasHandlers {
		HandlerRegistration addBackFromTokenHandler(BackFromPlaceHandler handler);
	}

	@Override
	protected void dispatch(BackFromPlaceHandler handler) {
		handler.onBackFromPlace(this);
	}

	@Override
	public Type<BackFromPlaceHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<BackFromPlaceHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new BackFromPlaceEvent());
	}

	public String getIdPratica() {
		return idPratica;
	}

}
