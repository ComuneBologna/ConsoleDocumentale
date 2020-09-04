package it.eng.portlet.consolepec.gwt.client.event.cartellafirma;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

/**
 *
 * @author biagiot
 *
 */
public class GoToWorklistCartellaFirmaEvent extends GwtEvent<GoToWorklistCartellaFirmaEvent.GoToWorklistCartellaFirmaHandler> {

	public static Type<GoToWorklistCartellaFirmaHandler> TYPE = new Type<GoToWorklistCartellaFirmaHandler>();

	public interface GoToWorklistCartellaFirmaHandler extends EventHandler {
		void onGoToWorklistCartellaFirma(GoToWorklistCartellaFirmaEvent event);
	}

	@Override
	public Type<GoToWorklistCartellaFirmaHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<GoToWorklistCartellaFirmaHandler> getType() {
		return TYPE;
	}

	@Override
	protected void dispatch(GoToWorklistCartellaFirmaHandler handler) {
		handler.onGoToWorklistCartellaFirma(this);
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new GoToWorklistCartellaFirmaEvent());
	}
}