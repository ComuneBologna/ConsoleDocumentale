package it.eng.portlet.consolepec.gwt.client.event;

import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ApertoDettaglioEvent extends GwtEvent<ApertoDettaglioEvent.DecrementaNumeroNonLettiHandler> {

	public static Type<DecrementaNumeroNonLettiHandler> TYPE = new Type<DecrementaNumeroNonLettiHandler>();
	private Boolean reloadWorklist;
	private PraticaDTO pratica;

	public interface DecrementaNumeroNonLettiHandler extends EventHandler {
		void onDettaglioAperto(ApertoDettaglioEvent event);
	}

	public ApertoDettaglioEvent(PraticaDTO pratica) {
		this.setPratica(pratica);
	}

	public ApertoDettaglioEvent(PraticaDTO pratica, Boolean reloadWorklist) {
		this.setPratica(pratica);
		this.reloadWorklist = reloadWorklist;
	}

	@Override
	protected void dispatch(DecrementaNumeroNonLettiHandler handler) {
		handler.onDettaglioAperto(this);
	}

	@Override
	public Type<DecrementaNumeroNonLettiHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<DecrementaNumeroNonLettiHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, PraticaDTO pratica) {
		source.fireEvent(new ApertoDettaglioEvent(pratica));
	}

	public static void fire(HasHandlers source, PraticaDTO pratica, Boolean reloadWorklist) {
		source.fireEvent(new ApertoDettaglioEvent(pratica, reloadWorklist));
	}

	public PraticaDTO getPratica() {
		return pratica;
	}

	public void setPratica(PraticaDTO pratica) {
		this.pratica = pratica;
	}

	public Boolean isReloadWorklist() {
		return reloadWorklist;
	}

	public void setReloadWorklist(Boolean reloadWorklist) {
		this.reloadWorklist = reloadWorklist;
	}
}