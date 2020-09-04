package it.eng.portlet.consolepec.gwt.client.drive.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import it.eng.portlet.consolepec.gwt.client.drive.event.CaricaFileEvent.CaricaFileEventHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Giacomo F.M.
 * @since 2019-06-27
 */
@AllArgsConstructor
public class CaricaFileEvent extends GwtEvent<CaricaFileEventHandler> {

	@Getter
	public static Type<CaricaFileEventHandler> type = new Type<>();

	public static interface CaricaFileEventHandler extends EventHandler {
		void onCaricaFile(CaricaFileEvent creaCartellaEvent);
	}

	@Getter
	private String idCartella;

	@Override
	public Type<CaricaFileEventHandler> getAssociatedType() {
		return type;
	}

	@Override
	protected void dispatch(CaricaFileEventHandler handler) {
		handler.onCaricaFile(this);
	}

}
