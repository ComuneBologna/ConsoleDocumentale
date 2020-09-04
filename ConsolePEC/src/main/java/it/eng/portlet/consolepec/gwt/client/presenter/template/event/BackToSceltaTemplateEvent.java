package it.eng.portlet.consolepec.gwt.client.presenter.template.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class BackToSceltaTemplateEvent extends GwtEvent<BackToSceltaTemplateEvent.BackToSceltaTemplateHandler> {

	public static Type<BackToSceltaTemplateHandler> TYPE = new Type<BackToSceltaTemplateHandler>();

	public interface BackToSceltaTemplateHandler extends EventHandler {
		void onBackToSceltaTemplate(BackToSceltaTemplateEvent event);
	}

	public BackToSceltaTemplateEvent() {
		super();
	}
	
	@Override
	protected void dispatch(BackToSceltaTemplateHandler handler) {
		handler.onBackToSceltaTemplate(this);
	}

	@Override
	public Type<BackToSceltaTemplateHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<BackToSceltaTemplateHandler> getType() {
		return TYPE;
	}
	
	

}
