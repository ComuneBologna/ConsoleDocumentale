package it.eng.portlet.consolepec.gwt.client.event.confermaprotocollazione;

import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CreaFascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.DatiDefaultProtocollazione;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public class ConfermaProtocollazionePecInEvent extends GwtEvent<ConfermaProtocollazionePecInEvent.ConfermaProtocollazionePecInHandler> {

	public static Type<ConfermaProtocollazionePecInHandler> TYPE = new Type<ConfermaProtocollazionePecInHandler>();

	private CreaFascicoloDTO creaFascicoloDTO;
	private String idEmailIn;
	private DatiDefaultProtocollazione datiPerFormProtocollazione;

	public interface ConfermaProtocollazionePecInHandler extends EventHandler {
		void onConfermaProtocollazionePecIn(ConfermaProtocollazionePecInEvent event);
	}

	public interface ConfermaProtocollazionePecInHasHandlers extends HasHandlers {
		HandlerRegistration addConfermaProtocollazionePecInHandler(ConfermaProtocollazionePecInHandler handler);
	}

	public ConfermaProtocollazionePecInEvent() {
	}

	@Override
	protected void dispatch(ConfermaProtocollazionePecInHandler handler) {
		handler.onConfermaProtocollazionePecIn(this);
	}

	@Override
	public Type<ConfermaProtocollazionePecInHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ConfermaProtocollazionePecInHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new ConfermaProtocollazionePecInEvent());
	}

	public CreaFascicoloDTO getCreaFascicoloDTO() {
		return creaFascicoloDTO;
	}

	public void setCreaFascicoloDTO(CreaFascicoloDTO creaFascicoloDTO) {
		this.creaFascicoloDTO = creaFascicoloDTO;
	}

	public String getIdEmailIn() {
		return idEmailIn;
	}

	public void setIdEmailIn(String idEmailIn) {
		this.idEmailIn = idEmailIn;
	}

	public DatiDefaultProtocollazione getDatiPerFormProtocollazione() {
		return datiPerFormProtocollazione;
	}

	public void setDatiPerFormProtocollazione(DatiDefaultProtocollazione datiPerFormProtocollazione) {
		this.datiPerFormProtocollazione = datiPerFormProtocollazione;
	}

}
