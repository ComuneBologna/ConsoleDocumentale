package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event;

import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.GestioneProcedimentoResult;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class RiepilogoAvvioChiusuraProcedimentoEvent extends GwtEvent<RiepilogoAvvioChiusuraProcedimentoEvent.RiepilogoAvvioChiusuraProcedimentoHandler> {

	public static Type<RiepilogoAvvioChiusuraProcedimentoHandler> TYPE = new Type<RiepilogoAvvioChiusuraProcedimentoHandler>();

	public interface RiepilogoAvvioChiusuraProcedimentoHandler extends EventHandler {
		void onRiepilogoAvvioChiusuraProcedimento(RiepilogoAvvioChiusuraProcedimentoEvent event);
	}

	private GestioneProcedimentoResult gestioneProcedimentoResult;
	private String idFascicolo;

	public RiepilogoAvvioChiusuraProcedimentoEvent(GestioneProcedimentoResult gestioneProcedimentoResult, String idFascicolo) {
		this.gestioneProcedimentoResult = gestioneProcedimentoResult;
		this.idFascicolo = idFascicolo;
	}

	public String getIdFascicolo() {
		return idFascicolo;
	}

	@Override
	protected void dispatch(RiepilogoAvvioChiusuraProcedimentoHandler handler) {
		handler.onRiepilogoAvvioChiusuraProcedimento(this);
	}

	@Override
	public Type<RiepilogoAvvioChiusuraProcedimentoHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<RiepilogoAvvioChiusuraProcedimentoHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, GestioneProcedimentoResult gestioneProcedimentoResult, String idFascicolo) {
		source.fireEvent(new RiepilogoAvvioChiusuraProcedimentoEvent(gestioneProcedimentoResult, idFascicolo));
	}

	public GestioneProcedimentoResult getGestioneProcedimentoResult() {
		return gestioneProcedimentoResult;
	}

}
