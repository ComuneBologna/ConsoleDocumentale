package it.eng.portlet.consolepec.gwt.client.event;

import it.eng.portlet.consolepec.gwt.shared.model.DatiDefaultProtocollazione;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class MostraDettaglioPraticaEvent extends GwtEvent<MostraDettaglioPraticaEvent.MostraDettaglioPraticaHandler> {

	public static Type<MostraDettaglioPraticaHandler> TYPE = new Type<MostraDettaglioPraticaHandler>();
	private Integer idPratica;

	public interface MostraDettaglioPraticaHandler extends EventHandler {
		void onMostraDettaglioPratica(MostraDettaglioPraticaEvent event);
	}

	public MostraDettaglioPraticaEvent(Integer idPratica) {
		this.idPratica = idPratica;
	}

	public MostraDettaglioPraticaEvent(Integer idPratica, Boolean fromConferma) {
		this.idPratica = idPratica;
		this.fromConferma = fromConferma;
	}

	public Integer getIdPratica() {
		return idPratica;
	}

	public Boolean fromConferma = false;
	private DatiDefaultProtocollazione datiPerProtocollazione;

	@Override
	protected void dispatch(MostraDettaglioPraticaHandler handler) {
		handler.onMostraDettaglioPratica(this);
	}

	@Override
	public Type<MostraDettaglioPraticaHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraDettaglioPraticaHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Integer idPratica) {
		source.fireEvent(new MostraDettaglioPraticaEvent(idPratica));
	}

	public void setDatiPerProtocollazione(DatiDefaultProtocollazione datiPerProtocollazione) {
		this.datiPerProtocollazione = datiPerProtocollazione;
	}

	public DatiDefaultProtocollazione getDatiPerProtocollazione() {
		return this.datiPerProtocollazione;
	}
}
