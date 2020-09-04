package it.eng.portlet.consolepec.gwt.client.presenter.pecout.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class MostraDettaglioPecOutInviataEvent
	extends
	GwtEvent<MostraDettaglioPecOutInviataEvent.MostraDettaglioPecOutInviataHandler> {
	
	private String idPratica;
	private String onChiudiToken;
	
	public static Type<MostraDettaglioPecOutInviataHandler> TYPE =
		new Type<MostraDettaglioPecOutInviataHandler>();

	public interface MostraDettaglioPecOutInviataHandler extends EventHandler {
	
		void onMostraDettaglioPecOutInviata(MostraDettaglioPecOutInviataEvent event);
	}

	public MostraDettaglioPecOutInviataEvent() {
	
	}
	

	@Override
	protected void dispatch(MostraDettaglioPecOutInviataHandler handler) {
	
		handler.onMostraDettaglioPecOutInviata(this);
	}

	@Override
	public Type<MostraDettaglioPecOutInviataHandler> getAssociatedType() {
	
		return TYPE;
	}

	public static Type<MostraDettaglioPecOutInviataHandler> getType() {
	
		return TYPE;
	}

	public static void fire(HasHandlers source) {
	
		source.fireEvent(new MostraDettaglioPecOutInviataEvent());
	}
	public void setIdPratica(String clientID) {
		this.idPratica = clientID;
	}

	
	public String getIdPratica() {
	
		return idPratica;
	}

	public String getOnChiudiToken() {
		return onChiudiToken;
	}
	public void setOnChiudiToken(String onChiudiToken){
		this.onChiudiToken = onChiudiToken;
	}
}
