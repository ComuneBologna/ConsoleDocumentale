package it.eng.portlet.consolepec.gwt.client.drive.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import it.eng.cobo.consolepec.commons.drive.File;
import it.eng.portlet.consolepec.gwt.client.drive.event.AggiornaFileEvent.AggiornaFileEventHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Giacomo F.M.
 * @since 2019-06-12
 */
@AllArgsConstructor
public class AggiornaFileEvent extends GwtEvent<AggiornaFileEventHandler> {

	public static final Type<AggiornaFileEventHandler> TYPE = new Type<>();

	public static interface AggiornaFileEventHandler extends EventHandler {
		void aggiornaFile(File file, boolean reload);
	}

	@Getter
	private File file;
	@Getter
	private boolean reload;

	@Override
	public Type<AggiornaFileEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AggiornaFileEventHandler handler) {
		handler.aggiornaFile(file, reload);
	}

}
