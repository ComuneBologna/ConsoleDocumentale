package it.eng.portlet.consolepec.gwt.client.drive.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import it.eng.cobo.consolepec.commons.drive.File;
import it.eng.portlet.consolepec.gwt.client.drive.event.ScaricaFileEvent.ScaricaFileEventHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Giacomo F.M.
 * @since 2019-06-05
 */
@AllArgsConstructor
public class ScaricaFileEvent extends GwtEvent<ScaricaFileEventHandler> {

	public static final Type<ScaricaFileEventHandler> TYPE = new Type<>();

	public static interface ScaricaFileEventHandler extends EventHandler {
		void scaricaFile(File file);
	}

	@Getter
	private File file;

	@Override
	public Type<ScaricaFileEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ScaricaFileEventHandler handler) {
		handler.scaricaFile(file);
	}

}
