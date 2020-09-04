package it.eng.portlet.consolepec.gwt.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class MostraDettaglioEvent extends GwtEvent<MostraDettaglioEvent.MostraDettaglioHandler> {

	public static Type<MostraDettaglioHandler> TYPE = new Type<MostraDettaglioHandler>();

	public interface MostraDettaglioHandler extends EventHandler {
		void onMostraDettaglio(MostraDettaglioEvent event);
	}

	private Integer id;

	public MostraDettaglioEvent(Integer id) {
		this.id = id;
	}

	public MostraDettaglioEvent() {
	}

	@Override
	protected void dispatch(MostraDettaglioHandler handler) {
		handler.onMostraDettaglio(this);
	}

	@Override
	public Type<MostraDettaglioHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraDettaglioHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new MostraDettaglioEvent());
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
