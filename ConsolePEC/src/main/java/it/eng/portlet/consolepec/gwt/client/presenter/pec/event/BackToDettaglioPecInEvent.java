package it.eng.portlet.consolepec.gwt.client.presenter.pec.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class BackToDettaglioPecInEvent extends GwtEvent<BackToDettaglioPecInEvent.BackToDettaglioPecInHandler> {

	public static Type<BackToDettaglioPecInHandler> TYPE = new Type<BackToDettaglioPecInHandler>();
	
	public interface BackToDettaglioPecInHandler extends EventHandler {
		void onBackToDettaglioPecIn(BackToDettaglioPecInEvent event);
	}

	private String pecPath;
	
	public BackToDettaglioPecInEvent(String pecPath) {
		super();
		this.pecPath = pecPath;
	}

	@Override
	protected void dispatch(BackToDettaglioPecInHandler handler) {
		handler.onBackToDettaglioPecIn(this);
	}

	@Override
	public Type<BackToDettaglioPecInHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<BackToDettaglioPecInHandler> getType() {
		return TYPE;
	}

	public String getPecPath() {
		return pecPath;
	}
	
	

}
