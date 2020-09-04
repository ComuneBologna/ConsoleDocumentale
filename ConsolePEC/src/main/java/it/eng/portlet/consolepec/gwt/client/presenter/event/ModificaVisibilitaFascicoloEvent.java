package it.eng.portlet.consolepec.gwt.client.presenter.event;

import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ModificaVisibilitaFascicoloEvent extends GwtEvent<ModificaVisibilitaFascicoloEvent.ModificaVisibilitaFascicoloHandler> {

	public static Type<ModificaVisibilitaFascicoloHandler> TYPE = new Type<ModificaVisibilitaFascicoloHandler>();
	private FascicoloDTO fascicolo;

	public interface ModificaVisibilitaFascicoloHandler extends EventHandler {
		void onModificaVisibilitaFascicolo(ModificaVisibilitaFascicoloEvent event);
	}
	
	public ModificaVisibilitaFascicoloEvent(FascicoloDTO fascicolo) {
		super();
		this.fascicolo = fascicolo;
	}

	public FascicoloDTO getFascicolo() {
		return fascicolo;
	}

	public void setFascicolo(FascicoloDTO fascicolo) {
		this.fascicolo = fascicolo;
	}

	@Override
	protected void dispatch(ModificaVisibilitaFascicoloHandler handler) {
		handler.onModificaVisibilitaFascicolo(this);
	}

	@Override
	public Type<ModificaVisibilitaFascicoloHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ModificaVisibilitaFascicoloHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, FascicoloDTO fascicolo) {
		source.fireEvent(new ModificaVisibilitaFascicoloEvent(fascicolo));
	}

}
