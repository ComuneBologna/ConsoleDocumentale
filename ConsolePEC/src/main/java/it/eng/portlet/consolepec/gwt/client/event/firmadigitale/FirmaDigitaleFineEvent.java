package it.eng.portlet.consolepec.gwt.client.event.firmadigitale;

import it.eng.portlet.consolepec.gwt.client.event.ClosingEvent;
import it.eng.portlet.consolepec.gwt.shared.action.firma.CredenzialiFirma;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO.TipologiaFirma;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

/**
 *
 * @author biagiot
 *
 */
public class FirmaDigitaleFineEvent extends GwtEvent<FirmaDigitaleFineEvent.FirmaDigitaleFineHandler> implements ClosingEvent{

	public static Type<FirmaDigitaleFineHandler> TYPE = new Type<FirmaDigitaleFineEvent.FirmaDigitaleFineHandler>();

	private Object openingRequestor;
	private CredenzialiFirma credenzialiFirma;
	private TipologiaFirma tipologiaFirma;
	private boolean isAnnulla;

	public interface FirmaDigitaleFineHandler extends EventHandler {
		void onFirmaDigitaleEnd(FirmaDigitaleFineEvent event);
	}

	public FirmaDigitaleFineEvent(Object openingRequestor, boolean isAnnulla) {
		this.isAnnulla = true;
		this.openingRequestor = openingRequestor;
	}

	public FirmaDigitaleFineEvent(Object openingRequestor, CredenzialiFirma credenzialiFirma, TipologiaFirma tipologiaFirma) {
		this.isAnnulla = false;
		this.openingRequestor = openingRequestor;
		this.credenzialiFirma = credenzialiFirma;
		this.tipologiaFirma = tipologiaFirma;
	}

	@Override
	public Type<FirmaDigitaleFineHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<FirmaDigitaleFineHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Object openingRequestor, CredenzialiFirma credenzialiFirma, TipologiaFirma tipologiaFirma) {
		source.fireEvent(new FirmaDigitaleFineEvent(openingRequestor, credenzialiFirma, tipologiaFirma));
	}

	public static void fire(HasHandlers source, Object openingRequestor, boolean isAnnulla) {
		source.fireEvent(new FirmaDigitaleFineEvent(openingRequestor, isAnnulla));
	}

	@Override
	protected void dispatch(FirmaDigitaleFineHandler handler) {
		handler.onFirmaDigitaleEnd(this);
	}

	@Override
	public Object getOpeningRequestor() {
		return openingRequestor;
	}

	public CredenzialiFirma getCredenzialiFirma() {
		return credenzialiFirma;
	}

	public TipologiaFirma getTipologiaFirma() {
		return tipologiaFirma;
	}

	public boolean isAnnulla() {
		return isAnnulla;
	}
}
