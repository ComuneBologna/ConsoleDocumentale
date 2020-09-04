package it.eng.portlet.consolepec.gwt.client.event.cartellafirma;

import java.util.Set;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

/**
 *
 * @author biagiot
 *
 */
public class AssegnaFascicoliInizioEvent extends GwtEvent<AssegnaFascicoliInizioEvent.AssegnaFascicoliInizioHandler> {

	public static Type<AssegnaFascicoliInizioHandler> TYPE = new Type<AssegnaFascicoliInizioHandler>();

	private Set<String> clientIds;

	public interface AssegnaFascicoliInizioHandler extends EventHandler {
		void onGoToAssegnaFascicoli(AssegnaFascicoliInizioEvent event);
	}

	public AssegnaFascicoliInizioEvent(Set<String> clientIds) {
		this.clientIds = clientIds;
	}

	@Override
	public Type<AssegnaFascicoliInizioHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<AssegnaFascicoliInizioHandler> getType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AssegnaFascicoliInizioHandler handler) {
		handler.onGoToAssegnaFascicoli(this);
	}

	public static void fire(HasHandlers source, Set<String> clientIds) {
		source.fireEvent(new AssegnaFascicoliInizioEvent(clientIds));
	}

	public Set<String> getClientIds() {
		return clientIds;
	}
}
