package it.eng.portlet.consolepec.gwt.client.event;

import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class GoToPlaceEvent extends GwtEvent<GoToPlaceEvent.GoToPlaceHandler> {

	public static Type<GoToPlaceHandler> TYPE = new Type<GoToPlaceHandler>();

	private Place place;

	public interface GoToPlaceHandler extends com.google.gwt.event.shared.EventHandler {
		void onGoToPlace(GoToPlaceEvent event);
	}

	public GoToPlaceEvent(Place place) {
		this.place = place;
	}

	public Place getPlace() {
		return place;
	}

	@Override
	protected void dispatch(GoToPlaceHandler handler) {
		handler.onGoToPlace(this);
	}

	@Override
	public Type<GoToPlaceHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<GoToPlaceHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Place place) {
		source.fireEvent(new GoToPlaceEvent(place));
	}
}
