package it.eng.portlet.consolepec.gwt.client.event;

import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class MostraSceltaOperatoreEvent extends GwtEvent<MostraSceltaOperatoreEvent.MostraSceltaOperatoreHandler> {

	public static Type<MostraSceltaOperatoreHandler> TYPE = new Type<MostraSceltaOperatoreHandler>();
	private PraticaDTO pratica;
	
	public interface MostraSceltaOperatoreHandler extends EventHandler {
		void onMostraSceltaOperatoreHandler(MostraSceltaOperatoreEvent event);
	}

	public MostraSceltaOperatoreEvent(PraticaDTO pratica) {
		super();
		this.pratica = pratica;
	}

	@Override
	protected void dispatch(MostraSceltaOperatoreHandler handler) {
		handler.onMostraSceltaOperatoreHandler(this);
	}

	@Override
	public Type<MostraSceltaOperatoreHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraSceltaOperatoreHandler> getType() {
		return TYPE;
	}

	public PraticaDTO getPratica() {
		return pratica;
	}

	
}
