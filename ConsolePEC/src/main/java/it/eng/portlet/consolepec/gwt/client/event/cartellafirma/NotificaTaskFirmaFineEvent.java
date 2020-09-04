package it.eng.portlet.consolepec.gwt.client.event.cartellafirma;

import it.eng.portlet.consolepec.gwt.shared.action.cartellafirma.InformazioniNotificaTaskFirmaDTO;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class NotificaTaskFirmaFineEvent extends GwtEvent<NotificaTaskFirmaFineEvent.NotificaTaskFirmaFineHandler> {

	public static Type<NotificaTaskFirmaFineHandler> TYPE = new Type<NotificaTaskFirmaFineHandler>();

	private InformazioniNotificaTaskFirmaDTO infoNotificaTaskFirma;
	private boolean isAnnulla;
	
	public interface NotificaTaskFirmaFineHandler extends EventHandler {
		void onNotificaTaskFirmaFine(NotificaTaskFirmaFineEvent event);
	}

	public NotificaTaskFirmaFineEvent(InformazioniNotificaTaskFirmaDTO infoNotificaTaskFirma) {
		this.infoNotificaTaskFirma = infoNotificaTaskFirma;
		this.isAnnulla = false;
	}
	
	public NotificaTaskFirmaFineEvent(boolean isAnnulla) {
		this.isAnnulla = isAnnulla;
	}

	@Override
	protected void dispatch(NotificaTaskFirmaFineHandler handler) {
		handler.onNotificaTaskFirmaFine(this);
	}

	@Override
	public Type<NotificaTaskFirmaFineHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<NotificaTaskFirmaFineHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, InformazioniNotificaTaskFirmaDTO infoNotificaTaskFirma) {
		source.fireEvent(new NotificaTaskFirmaFineEvent(infoNotificaTaskFirma));
	}
	
	public static void fire(HasHandlers source, boolean isAnnulla) {
		source.fireEvent(new NotificaTaskFirmaFineEvent(isAnnulla));
	}
	
	public boolean isAnnulla() {
		return isAnnulla;
	}

	public InformazioniNotificaTaskFirmaDTO getInfoNotificaTaskFirma() {
		return infoNotificaTaskFirma;
	}
}