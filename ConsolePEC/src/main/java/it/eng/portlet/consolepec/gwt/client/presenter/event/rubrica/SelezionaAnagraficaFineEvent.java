package it.eng.portlet.consolepec.gwt.client.presenter.event.rubrica;


import it.eng.cobo.consolepec.commons.rubrica.Anagrafica;
import it.eng.portlet.consolepec.gwt.client.event.ClosingEvent;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;


/**
 * @author AlessandroP
 * @since 08/11/2017
 */
public class SelezionaAnagraficaFineEvent extends GwtEvent<SelezionaAnagraficaFineEvent.SelezionaAnagraficaFineHandler> implements ClosingEvent {

	private static Type<SelezionaAnagraficaFineHandler> TYPE = new Type<SelezionaAnagraficaFineHandler>();
	
	private Anagrafica anagrafica;
	private boolean annulla;
	private String nomeDatoAggiuntivo;
	
	Object openingRequestor;
	
	public SelezionaAnagraficaFineEvent(Object openingRequestor, Anagrafica anagrafica) {
		super();
		this.openingRequestor = openingRequestor;
		this.anagrafica = anagrafica;
		this.annulla = false;
	}
	
	public SelezionaAnagraficaFineEvent(Object openingRequestor) {
		super();
		this.openingRequestor = openingRequestor;
		this.anagrafica = null;
		this.annulla = true;
	}
	
	public interface SelezionaAnagraficaFineHandler extends EventHandler {
		void onAnagraficaSelezionata(SelezionaAnagraficaFineEvent event);
	}

	@Override
	public Type<SelezionaAnagraficaFineHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<SelezionaAnagraficaFineHandler> getType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SelezionaAnagraficaFineHandler handler) {
		handler.onAnagraficaSelezionata(this);
	}

	public boolean isAnnulla() {
		return annulla;
	}

	public void setAnnulla(boolean annulla) {
		this.annulla = annulla;
	}
	
	public void setAnagrafica(Anagrafica anagrafica) {
		this.anagrafica = anagrafica;
	}
	
	public Anagrafica getAnagrafica(){
		return this.anagrafica;
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
}
