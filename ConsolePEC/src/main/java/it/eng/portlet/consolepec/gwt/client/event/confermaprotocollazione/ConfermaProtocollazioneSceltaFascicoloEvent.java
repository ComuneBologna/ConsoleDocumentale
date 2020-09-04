package it.eng.portlet.consolepec.gwt.client.event.confermaprotocollazione;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public class ConfermaProtocollazioneSceltaFascicoloEvent extends GwtEvent<ConfermaProtocollazioneSceltaFascicoloEvent.ConfermaProtocollazioneSceltaFascicoloHandler> {

	public static Type<ConfermaProtocollazioneSceltaFascicoloHandler> TYPE = new Type<ConfermaProtocollazioneSceltaFascicoloHandler>();

	public interface ConfermaProtocollazioneSceltaFascicoloHandler extends EventHandler {
		void onConfermaProtocollazioneSceltaFascicolo(ConfermaProtocollazioneSceltaFascicoloEvent event);
	}

	public interface ConfermaProtocollazioneSceltaFascicoloHasHandlers extends HasHandlers {
		HandlerRegistration addConfermaProtocollazioneSceltaFascicoloHandler(ConfermaProtocollazioneSceltaFascicoloHandler handler);
	}

	private String idFascicolo;
	private String idPraticaModulistica;
	private String idEmail;
	private String provenienza;

	public ConfermaProtocollazioneSceltaFascicoloEvent() {
	}

	@Override
	protected void dispatch(ConfermaProtocollazioneSceltaFascicoloHandler handler) {
		handler.onConfermaProtocollazioneSceltaFascicolo(this);
	}

	@Override
	public Type<ConfermaProtocollazioneSceltaFascicoloHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ConfermaProtocollazioneSceltaFascicoloHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new ConfermaProtocollazioneSceltaFascicoloEvent());
	}

	public String getIdFascicolo() {
		return idFascicolo;
	}

	public void setIdFascicolo(String idFascicolo) {
		this.idFascicolo = idFascicolo;
	}

	
	public String getIdPraticaModulistica() {
		return idPraticaModulistica;
	}

	public void setIdPraticaModulistica(String idPraticaModulistica) {
		this.idPraticaModulistica = idPraticaModulistica;
	}

	public String getIdEmail() {
		return idEmail;
	}

	public void setIdEmail(String idEmail) {
		this.idEmail = idEmail;
	}

	public String getProvenienza() {
		return provenienza;
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}

}
