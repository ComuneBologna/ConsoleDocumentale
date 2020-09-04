package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.sceltacapofila;

import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.sceltacapofila.MostraSceltaCapofilaEvent.SelectedObject;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;

import java.util.Set;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public class MostraSceltaCapofilaProtocollazioneFascicoloEvent extends GwtEvent<MostraSceltaCapofilaProtocollazioneFascicoloEvent.MostraSceltaCapofilaProtocollazioneFascicoloHandler> {

	public static Type<MostraSceltaCapofilaProtocollazioneFascicoloHandler> TYPE = new Type<MostraSceltaCapofilaProtocollazioneFascicoloHandler>();

	private String idFascicolo;
	private Set<SelectedObject> listEmail;
	private Set<AllegatoDTO> allegati;

	public interface MostraSceltaCapofilaProtocollazioneFascicoloHandler extends EventHandler {
		void onMostraSceltaCapofilaProtocollazioneFascicolo(MostraSceltaCapofilaProtocollazioneFascicoloEvent event);
	}

	public interface MostraSceltaCapofilaProtocollazioneFascicoloHasHandlers extends HasHandlers {
		HandlerRegistration addMostraSceltaCapofilaProtocollazioneFascicoloHandler(MostraSceltaCapofilaProtocollazioneFascicoloHandler handler);
	}

	public MostraSceltaCapofilaProtocollazioneFascicoloEvent() {
	}

	@Override
	protected void dispatch(MostraSceltaCapofilaProtocollazioneFascicoloHandler handler) {
		handler.onMostraSceltaCapofilaProtocollazioneFascicolo(this);
	}

	@Override
	public Type<MostraSceltaCapofilaProtocollazioneFascicoloHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraSceltaCapofilaProtocollazioneFascicoloHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new MostraSceltaCapofilaProtocollazioneFascicoloEvent());
	}

	public String getIdFascicolo() {
		return idFascicolo;
	}

	public void setIdFascicolo(String idFascicolo) {
		this.idFascicolo = idFascicolo;
	}

	public Set<SelectedObject> getListEmail() {
		return listEmail;
	}

	public void setListEmail(Set<SelectedObject> listEmail) {
		this.listEmail = listEmail;
	}

	public Set<AllegatoDTO> getAllegati() {
		return allegati;
	}

	public void setAllegati(Set<AllegatoDTO> allegati) {
		this.allegati = allegati;
	}
}
