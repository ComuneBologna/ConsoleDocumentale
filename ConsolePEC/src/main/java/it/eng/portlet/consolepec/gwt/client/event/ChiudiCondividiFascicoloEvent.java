package it.eng.portlet.consolepec.gwt.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ChiudiCondividiFascicoloEvent extends GwtEvent<ChiudiCondividiFascicoloEvent.ChiudiCondividiFascicoloHandler> {

	public static Type<ChiudiCondividiFascicoloHandler> TYPE = new Type<ChiudiCondividiFascicoloHandler>();

	public interface ChiudiCondividiFascicoloHandler extends EventHandler {
		void onChiudiCondividiFascicolo(ChiudiCondividiFascicoloEvent event);
	}
	
	private String fascicoloPath;
	
	public ChiudiCondividiFascicoloEvent(String fascicoloPath) {
		this.fascicoloPath = fascicoloPath;
	}

	@Override
	protected void dispatch(ChiudiCondividiFascicoloHandler handler) {
		handler.onChiudiCondividiFascicolo(this);
	}

	@Override
	public Type<ChiudiCondividiFascicoloHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ChiudiCondividiFascicoloHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String fascicoloPath) {
		source.fireEvent(new ChiudiCondividiFascicoloEvent(fascicoloPath));
	}

	public String getFascicoloPath() {
		return fascicoloPath;
	}
	
}
