package it.eng.portlet.consolepec.gwt.client.drive.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import it.eng.portlet.consolepec.gwt.client.drive.event.ApriCartellaEvent.ApriCartellaEventHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Giacomo F.M.
 * @since 2019-06-05
 */
@AllArgsConstructor
public class ApriCartellaEvent extends GwtEvent<ApriCartellaEventHandler> {

	public static final Type<ApriCartellaEventHandler> TYPE = new Type<>();

	public static interface ApriCartellaEventHandler extends EventHandler {
		void apriCartella(String idCartella, Integer page);
	}

	@Getter
	private String idCartella;
	@Getter
	private Integer page;

	@Override
	public Type<ApriCartellaEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ApriCartellaEventHandler handler) {
		handler.apriCartella(idCartella, page);
	}

}
