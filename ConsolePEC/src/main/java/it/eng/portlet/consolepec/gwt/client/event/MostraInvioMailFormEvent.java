package it.eng.portlet.consolepec.gwt.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class MostraInvioMailFormEvent extends GwtEvent<MostraInvioMailFormEvent.MostraInvioMailFormHandler> {

	public static Type<MostraInvioMailFormHandler> TYPE = new Type<MostraInvioMailFormHandler>();

	public interface MostraInvioMailFormHandler extends EventHandler {
		void onMostraInvioMailForm(MostraInvioMailFormEvent event);
	}

	private String pathFascicolo;
	private String pathBozza;

	public MostraInvioMailFormEvent() {
	}

	public MostraInvioMailFormEvent(String pathFascicolo, String pathBozza) {
		this.pathBozza = pathBozza;
		this.pathFascicolo = pathFascicolo;
	}

	@Override
	protected void dispatch(MostraInvioMailFormHandler handler) {
		handler.onMostraInvioMailForm(this);
	}

	@Override
	public Type<MostraInvioMailFormHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraInvioMailFormHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new MostraInvioMailFormEvent());
	}

	public String getPathFascicolo() {
		return pathFascicolo;
	}

	public String getPathBozza() {
		return pathBozza;
	}

}
