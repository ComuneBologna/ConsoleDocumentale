package it.eng.portlet.consolepec.gwt.client.presenter.event.rubrica;

import it.eng.portlet.consolepec.gwt.client.event.OpeningEvent;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;


/**
 * @author AlessandroP
 * @since 21/10/2017
 */
public class SelezionaAnagraficaInizioEvent extends GwtEvent<SelezionaAnagraficaInizioEvent.SelezionaAnagraficaInizioHandler> implements OpeningEvent {

	private static Type<SelezionaAnagraficaInizioHandler> TYPE = new Type<SelezionaAnagraficaInizioHandler>();
	
	Object openingRequestor;
	String nomeDatoAggiuntivo;
	boolean selezione=true;

	public SelezionaAnagraficaInizioEvent(Object openingRequestor) {
		super();
		this.openingRequestor = openingRequestor;
	}

	public interface SelezionaAnagraficaInizioHandler extends EventHandler {
		void onMostraListaAnagraficheWidget(SelezionaAnagraficaInizioEvent event);
	}

	@Override
	public Type<SelezionaAnagraficaInizioHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<SelezionaAnagraficaInizioHandler> getType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SelezionaAnagraficaInizioHandler handler) {
		handler.onMostraListaAnagraficheWidget(this);
	}

	@Override
	public Object getOpeningRequestor() {
		return openingRequestor;
	}
	
	public String getNomeDatoAggiuntivo() {
		return nomeDatoAggiuntivo;
	}

	public void setNomeDatoAggiuntivo(String nomeDatoAggiuntivo) {
		this.nomeDatoAggiuntivo = nomeDatoAggiuntivo;
	}
	
	public boolean isSelezione() {
		return selezione;
	}

	public void setSelezione(boolean selezione) {
		this.selezione = selezione;
	}

	
}
