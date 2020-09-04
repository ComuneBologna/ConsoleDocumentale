package it.eng.portlet.consolepec.gwt.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class UpdateSiteMapEvent extends GwtEvent<UpdateSiteMapEvent.UpdateSiteMapHandler> {

	public static Type<UpdateSiteMapHandler> TYPE = new Type<UpdateSiteMapHandler>();
	private Boolean reloadWorklist;

	public interface UpdateSiteMapHandler extends EventHandler {
		void onUpdateSiteMap(UpdateSiteMapEvent event);
	}

	public UpdateSiteMapEvent() {

	}

	public UpdateSiteMapEvent(Boolean reloadWorklist) {
		this.reloadWorklist = reloadWorklist;
	}

	@Override
	protected void dispatch(UpdateSiteMapHandler handler) {
		handler.onUpdateSiteMap(this);
	}

	@Override
	public Type<UpdateSiteMapHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<UpdateSiteMapHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new UpdateSiteMapEvent());
	}

	public static void fire(HasHandlers source, Boolean reloadWorklist) {
		source.fireEvent(new UpdateSiteMapEvent(reloadWorklist));
	}

	public Boolean isReloadWorklist() {
		return reloadWorklist;
	}

	public void setReloadWorklist(Boolean reloadWorklist) {
		this.reloadWorklist = reloadWorklist;
	}
}
