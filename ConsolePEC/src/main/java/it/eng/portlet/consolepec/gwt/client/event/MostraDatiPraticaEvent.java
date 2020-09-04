package it.eng.portlet.consolepec.gwt.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class MostraDatiPraticaEvent extends GwtEvent<MostraDatiPraticaEvent.MostraDatiPraticaHandler> {

	public static Type<MostraDatiPraticaHandler> TYPE = new Type<MostraDatiPraticaHandler>();
	private boolean crea = false;
	private boolean svuotaCampi = true;
	private String clientID;

	public interface MostraDatiPraticaHandler extends EventHandler {
		void onMostraDatiPratica(MostraDatiPraticaEvent event);
	}

	public MostraDatiPraticaEvent(String id) {
		this.clientID = id;
	}

	@Override
	protected void dispatch(MostraDatiPraticaHandler handler) {
		handler.onMostraDatiPratica(this);
	}

	@Override
	public Type<MostraDatiPraticaHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraDatiPraticaHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String id) {
		source.fireEvent(new MostraDatiPraticaEvent(id));
	}

	/**
	 * 
	 * @param crea
	 *            indica se si vuol partire per la creazione di un nuovo fascicolo
	 */
	public void setCreazioneFascicolo(boolean crea) {
		this.crea = crea;
	}

	public Boolean isCreazioneFascicolo() {
		return this.crea;
	}

	public boolean isSvuotaCampi() {
		return svuotaCampi;
	}

	public void setSvuotaCampi(boolean svuotaCampi) {
		this.svuotaCampi = svuotaCampi;
	}

	public String getClientID() {
		return clientID;
	}

}
