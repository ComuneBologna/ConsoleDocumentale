package it.eng.portlet.consolepec.gwt.client.presenter.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public class GoToModificaAbilitazioniAssegnaEsternoFromDettaglioFascicoloEvent extends GwtEvent<GoToModificaAbilitazioniAssegnaEsternoFromDettaglioFascicoloEvent.GoToModificaAbilitazioniAssegnaEsternoFromDettaglioFascicoloHandler> {

	public static Type<GoToModificaAbilitazioniAssegnaEsternoFromDettaglioFascicoloHandler> TYPE = new Type<GoToModificaAbilitazioniAssegnaEsternoFromDettaglioFascicoloHandler>();
	private String idFascicolo;

	public interface GoToModificaAbilitazioniAssegnaEsternoFromDettaglioFascicoloHandler extends EventHandler {
		void onGoToModificaAbilitazioniAssegnaEsternoFromDettaglioFascicolo(GoToModificaAbilitazioniAssegnaEsternoFromDettaglioFascicoloEvent event);
	}

	public interface GoToAssegnaFromDettaglioFascicoloHasHandlers extends HasHandlers {
		HandlerRegistration addGoToAssegnaFromDettaglioFascicoloHandler(GoToModificaAbilitazioniAssegnaEsternoFromDettaglioFascicoloHandler handler);
	}

	public GoToModificaAbilitazioniAssegnaEsternoFromDettaglioFascicoloEvent(String idFascicolo) {
		this.idFascicolo = idFascicolo;
	}

	public String getIdFascicolo() {
		return idFascicolo;
	}

	@Override
	protected void dispatch(GoToModificaAbilitazioniAssegnaEsternoFromDettaglioFascicoloHandler handler) {
		handler.onGoToModificaAbilitazioniAssegnaEsternoFromDettaglioFascicolo(this);
	}

	@Override
	public Type<GoToModificaAbilitazioniAssegnaEsternoFromDettaglioFascicoloHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<GoToModificaAbilitazioniAssegnaEsternoFromDettaglioFascicoloHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String idFascicolo) {
		source.fireEvent(new GoToModificaAbilitazioniAssegnaEsternoFromDettaglioFascicoloEvent(idFascicolo));
	}
}
