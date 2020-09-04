package it.eng.portlet.consolepec.gwt.client.presenter.event;

import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ModificaVisibilitaAllegatoEvent extends GwtEvent<ModificaVisibilitaAllegatoEvent.ModificaVisibilitaAllegatoHandler> {

	public static Type<ModificaVisibilitaAllegatoHandler> TYPE = new Type<ModificaVisibilitaAllegatoHandler>();
	private AllegatoDTO allegatoDTO;
	private FascicoloDTO fascicolo;

	public interface ModificaVisibilitaAllegatoHandler extends EventHandler {
		void onModificaVisibilitaAllegato(ModificaVisibilitaAllegatoEvent event);
	}

	public ModificaVisibilitaAllegatoEvent(AllegatoDTO allegatoDTO, FascicoloDTO fascicoloDTO) {
		this.allegatoDTO = allegatoDTO;
		this.fascicolo = fascicoloDTO;
	}

	public AllegatoDTO getAllegatoDTO() {
		return allegatoDTO;
	}

	@Override
	protected void dispatch(ModificaVisibilitaAllegatoHandler handler) {
		handler.onModificaVisibilitaAllegato(this);
	}

	@Override
	public Type<ModificaVisibilitaAllegatoHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ModificaVisibilitaAllegatoHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, AllegatoDTO allegatoDTO, FascicoloDTO fascicoloDTO) {
		source.fireEvent(new ModificaVisibilitaAllegatoEvent(allegatoDTO, fascicoloDTO));
	}

	public FascicoloDTO getFascicolo() {
		return fascicolo;
	}

}
