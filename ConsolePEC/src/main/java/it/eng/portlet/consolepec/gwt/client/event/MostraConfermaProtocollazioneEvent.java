package it.eng.portlet.consolepec.gwt.client.event;

import it.eng.portlet.consolepec.gwt.shared.model.DatiDefaultProtocollazione;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class MostraConfermaProtocollazioneEvent extends GwtEvent<MostraConfermaProtocollazioneEvent.MostraConfermaProtocollazioneHandler> {

	public static Type<MostraConfermaProtocollazioneHandler> TYPE = new Type<MostraConfermaProtocollazioneHandler>();
	private DatiDefaultProtocollazione datiPerFormProtocollazione;
	private boolean fromPecOut;
	private boolean fromSceltaFascicolo;
	private String idFascicoloSelezionato;
	private String idPraticaDaAggiungere;
	private String idPraticaMailOut;

	public interface MostraConfermaProtocollazioneHandler extends EventHandler {
		void onMostraConfermaProtocollazione(MostraConfermaProtocollazioneEvent event);
	}

	public MostraConfermaProtocollazioneEvent() {
	}

	@Override
	protected void dispatch(MostraConfermaProtocollazioneHandler handler) {
		handler.onMostraConfermaProtocollazione(this);
	}

	@Override
	public Type<MostraConfermaProtocollazioneHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraConfermaProtocollazioneHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, MostraConfermaProtocollazioneEvent event) {
		source.fireEvent(event);
	}

	public DatiDefaultProtocollazione getDatiPerFormProtocollazione() {
		return datiPerFormProtocollazione;
	}

	public void setDatiPerFormProtocollazione(DatiDefaultProtocollazione datiPerFormProtocollazione) {
		this.datiPerFormProtocollazione = datiPerFormProtocollazione;
	}

	public boolean isFromPecOut() {
		return fromPecOut;
	}

	public void setFromPecOut(boolean fromPecOut) {
		this.fromPecOut = fromPecOut;
	}

	public boolean isFromSceltaFascicolo() {
		return fromSceltaFascicolo;
	}

	public void setFromSceltaFascicolo(boolean fromSceltaFascicolo) {
		this.fromSceltaFascicolo = fromSceltaFascicolo;
	}

	public String getIdFascicoloSelezionato() {
		return idFascicoloSelezionato;
	}

	public void setIdFascicoloSelezionato(String idFascicoloSelezionato) {
		this.idFascicoloSelezionato = idFascicoloSelezionato;
	}

	public String getIdPraticaDaAggiungere() {
		return idPraticaDaAggiungere;
	}

	public void setIdPraticaDaAggiungere(String idPraticaDaAggiungere) {
		this.idPraticaDaAggiungere = idPraticaDaAggiungere;
	}

	public String getIdPraticaMailOut() {
		return idPraticaMailOut;
	}

	public void setIdPraticaMailOut(String idPraticaMailOut) {
		this.idPraticaMailOut = idPraticaMailOut;
	}

}
