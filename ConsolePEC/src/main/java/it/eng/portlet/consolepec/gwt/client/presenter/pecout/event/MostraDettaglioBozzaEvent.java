package it.eng.portlet.consolepec.gwt.client.presenter.pecout.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class MostraDettaglioBozzaEvent
	extends GwtEvent<MostraDettaglioBozzaEvent.MostraDettaglioBozzaHandler> {

	public static Type<MostraDettaglioBozzaHandler> TYPE =
		new Type<MostraDettaglioBozzaHandler>();

	public interface MostraDettaglioBozzaHandler extends EventHandler {
	
		void onMostraDettaglioBozza(MostraDettaglioBozzaEvent event);
	}

	private String idPratica;
	private String onChiudiToken;
	private boolean interoperabile;
	
	public MostraDettaglioBozzaEvent() {
	
	}

	@Override
	protected void dispatch(MostraDettaglioBozzaHandler handler) {
	
		handler.onMostraDettaglioBozza(this);
	}

	@Override
	public Type<MostraDettaglioBozzaHandler> getAssociatedType() {
	
		return TYPE;
	}

	public static Type<MostraDettaglioBozzaHandler> getType() {
	
		return TYPE;
	}

	public static void fire(HasHandlers source) {
	
		source.fireEvent(new MostraDettaglioBozzaEvent());
	}

	public void setIdPratica(String clientID) {
		this.idPratica = clientID;
	}

	
	public String getIdPratica() {
	
		return idPratica;
	}

	public boolean isInteroperabile() {
		return interoperabile;
	}

	public void setInteroperabile(boolean interoperabile) {
		this.interoperabile = interoperabile;
	}

	public String getOnChiudiToken() {
		return onChiudiToken;
	}
	public void setOnChiudiToken(String onChiudiToken){
		this.onChiudiToken = onChiudiToken;
	}
}
