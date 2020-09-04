package it.eng.portlet.consolepec.gwt.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class MostraCondividiFascicoloEvent extends GwtEvent<MostraCondividiFascicoloEvent.MostraCondividiFascicoloHandler> {

	public static Type<MostraCondividiFascicoloHandler> TYPE = new Type<MostraCondividiFascicoloHandler>();

	public interface MostraCondividiFascicoloHandler extends EventHandler {
		void onMostraCondividiFascicolo(MostraCondividiFascicoloEvent event);
	}
	
	private String fascicoloPath;

	public MostraCondividiFascicoloEvent(String fascicoloPath) {
		this.fascicoloPath = fascicoloPath;
	}

	@Override
	protected void dispatch(MostraCondividiFascicoloHandler handler) {
		handler.onMostraCondividiFascicolo(this);
	}

	@Override
	public Type<MostraCondividiFascicoloHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraCondividiFascicoloHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String fascicoloPath) {
		source.fireEvent(new MostraCondividiFascicoloEvent(fascicoloPath));
	}

	public String getFascicoloPath() {
		return fascicoloPath;
	}

}
