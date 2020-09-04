package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event;

import it.eng.portlet.consolepec.gwt.shared.dto.Element;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class MostraEsitoProtocollazioneEvent extends GwtEvent<MostraEsitoProtocollazioneEvent.MostraEsitoProtocollazioneHandler> {

	public static Type<MostraEsitoProtocollazioneHandler> TYPE = new Type<MostraEsitoProtocollazioneHandler>();
	private String clientID;
	private Map<String, Map<String, Element>> map;
	private String pg;
	private String pgCapofila;
	private Set<String> praticheProtocollate = new HashSet<String>();

	public interface MostraEsitoProtocollazioneHandler extends EventHandler {

		void onMostraEsitoProtocollazione(MostraEsitoProtocollazioneEvent event);
	}

	public MostraEsitoProtocollazioneEvent(String clientID) {
		this.clientID = clientID;
	}

	public String getClientID() {
		return clientID;
	}

	@Override
	protected void dispatch(MostraEsitoProtocollazioneHandler handler) {
		handler.onMostraEsitoProtocollazione(this);
	}

	@Override
	public Type<MostraEsitoProtocollazioneHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraEsitoProtocollazioneHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String clientID) {
		source.fireEvent(new MostraEsitoProtocollazioneEvent(clientID));
	}

	public void setMapForm(Map<String, Map<String, Element>> map) {
		this.map = map;
	}

	public Map<String, Map<String, Element>> getMapForm() {
		return this.map;
	}

	public String getPG() {
		return this.pg;
	}

	public void setPg(String pg) {
		this.pg = pg;
	}

	public String getPgCapofila() {
		return pgCapofila;
	}

	public void setPgCapofila(String pgCapofila) {
		this.pgCapofila = pgCapofila;
	}

	public void setPraticheProtocollate(Set<String> pratiche) {
		this.praticheProtocollate = pratiche;
	}

	public Set<String> getPraticheProtocollate() {
		return praticheProtocollate;
	}

}
