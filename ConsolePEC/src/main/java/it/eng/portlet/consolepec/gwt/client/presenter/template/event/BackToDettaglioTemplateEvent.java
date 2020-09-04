package it.eng.portlet.consolepec.gwt.client.presenter.template.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class BackToDettaglioTemplateEvent extends GwtEvent<BackToDettaglioTemplateEvent.BackToDettaglioTemplateHanlder> {

	public static Type<BackToDettaglioTemplateHanlder> TYPE = new Type<BackToDettaglioTemplateHanlder>();

	public interface BackToDettaglioTemplateHanlder extends EventHandler {
		void onBackToDettaglioTemplate(BackToDettaglioTemplateEvent event);
	}

	public BackToDettaglioTemplateEvent() {
		super();
	}
	
	@Override
	protected void dispatch(BackToDettaglioTemplateHanlder handler) {
		handler.onBackToDettaglioTemplate(this);
	}

	@Override
	public Type<BackToDettaglioTemplateHanlder> getAssociatedType() {
		return TYPE;
	}

	public static Type<BackToDettaglioTemplateHanlder> getType() {
		return TYPE;
	}
	
	

}
