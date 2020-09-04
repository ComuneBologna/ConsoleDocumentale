package it.eng.portlet.consolepec.gwt.client.presenter.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class GoToConfermaMailDaTemplateEvent extends GwtEvent<GoToConfermaMailDaTemplateEvent.GoToConfermaMailDaTemplateHandler> {

	public static Type<GoToConfermaMailDaTemplateHandler> TYPE = new Type<GoToConfermaMailDaTemplateHandler>();
	private String fascicoloPath;

	public interface GoToConfermaMailDaTemplateHandler extends EventHandler {
		void onGoToConfermaMailDaTemplate(GoToConfermaMailDaTemplateEvent event);
	}

	public GoToConfermaMailDaTemplateEvent(String fascicoloPath) {
		this.fascicoloPath = fascicoloPath;
	}

	public String getFascicoloPath() {
		return fascicoloPath;
	}

	@Override
	protected void dispatch(GoToConfermaMailDaTemplateHandler handler) {
		handler.onGoToConfermaMailDaTemplate(this);
	}

	@Override
	public Type<GoToConfermaMailDaTemplateHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<GoToConfermaMailDaTemplateHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String fascicoloPath) {
		source.fireEvent(new GoToConfermaMailDaTemplateEvent(fascicoloPath));
	}
}
