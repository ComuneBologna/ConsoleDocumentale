package it.eng.portlet.consolepec.gwt.client.event.cartellafirma;

import it.eng.portlet.consolepec.gwt.shared.action.cartellafirma.InformazioniRiassegnazioneFascicoliTaskFirmaDTO;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

/**
 *
 * @author biagiot
 *
 */
public class AssegnaFascicoliFineEvent extends GwtEvent<AssegnaFascicoliFineEvent.AssegnaFascicoliFineHandler>{

	public static Type<AssegnaFascicoliFineHandler> TYPE = new Type<AssegnaFascicoliFineHandler>();

	private InformazioniRiassegnazioneFascicoliTaskFirmaDTO riassegnaFascicoloInfo;
	private boolean isAnnulla;

	public interface AssegnaFascicoliFineHandler extends EventHandler {
		void onAssegnaFascicoliFine(AssegnaFascicoliFineEvent event);
	}

	public AssegnaFascicoliFineEvent(InformazioniRiassegnazioneFascicoliTaskFirmaDTO riassegnaFascicoloInfo) {
		this.riassegnaFascicoloInfo = riassegnaFascicoloInfo;
		this.isAnnulla = false;
	}

	public AssegnaFascicoliFineEvent(boolean isAnnulla) {
		this.isAnnulla = isAnnulla;
	}

	@Override
	public Type<AssegnaFascicoliFineHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<AssegnaFascicoliFineHandler> getType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AssegnaFascicoliFineHandler handler) {
		handler.onAssegnaFascicoliFine(this);
	}

	public static void fire (HasHandlers source, InformazioniRiassegnazioneFascicoliTaskFirmaDTO riassegnaFascicoloInfo) {
		source.fireEvent(new AssegnaFascicoliFineEvent(riassegnaFascicoloInfo));
	}

	public static void fire (HasHandlers source, boolean isAnnulla) {
		source.fireEvent(new AssegnaFascicoliFineEvent(isAnnulla));
	}

	public boolean isAnnulla() {
		return isAnnulla;
	}

	public InformazioniRiassegnazioneFascicoliTaskFirmaDTO getRiassegnaFascicoloInfo() {
		return riassegnaFascicoloInfo;
	}

	public void setRiassegnaFascicoloInfo(InformazioniRiassegnazioneFascicoliTaskFirmaDTO riassegnaFascicoloInfo) {
		this.riassegnaFascicoloInfo = riassegnaFascicoloInfo;
	}
}
