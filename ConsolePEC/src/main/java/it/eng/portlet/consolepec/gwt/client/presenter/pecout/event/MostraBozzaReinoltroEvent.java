package it.eng.portlet.consolepec.gwt.client.presenter.pecout.event;

import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public class MostraBozzaReinoltroEvent extends GwtEvent<MostraBozzaReinoltroEvent.MostraBozzaReinoltroHandler> {

	public static Type<MostraBozzaReinoltroHandler> TYPE = new Type<MostraBozzaReinoltroHandler>();
	private PecOutDTO pecOutDto;

	public interface MostraBozzaReinoltroHandler extends EventHandler {
		void onMostraBozzaReinoltro(MostraBozzaReinoltroEvent event);
	}

	public interface MostraBozzaReinoltroHasHandlers extends HasHandlers {
		HandlerRegistration addMostraBozzaReinoltroHandler(MostraBozzaReinoltroHandler handler);
	}

	public MostraBozzaReinoltroEvent(PecOutDTO pecOutDto) {
		this.pecOutDto = pecOutDto;
	}

	public PecOutDTO getPecOutDto() {
		return pecOutDto;
	}

	@Override
	protected void dispatch(MostraBozzaReinoltroHandler handler) {
		handler.onMostraBozzaReinoltro(this);
	}

	@Override
	public Type<MostraBozzaReinoltroHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraBozzaReinoltroHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, PecOutDTO pecOutDto) {
		source.fireEvent(new MostraBozzaReinoltroEvent(pecOutDto));
	}
}
