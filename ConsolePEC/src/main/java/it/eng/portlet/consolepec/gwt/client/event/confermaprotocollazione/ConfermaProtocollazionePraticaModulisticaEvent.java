package it.eng.portlet.consolepec.gwt.client.event.confermaprotocollazione;

import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CreaFascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.DatiDefaultProtocollazione;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public class ConfermaProtocollazionePraticaModulisticaEvent extends GwtEvent<ConfermaProtocollazionePraticaModulisticaEvent.ConfermaProtocollazionePraticaModulisticaHandler> {

	public static Type<ConfermaProtocollazionePraticaModulisticaHandler> TYPE = new Type<ConfermaProtocollazionePraticaModulisticaHandler>();

	private CreaFascicoloDTO creaFascicoloDTO;
	private String idPraticaModulistica;
	private DatiDefaultProtocollazione datiPerFormProtocollazione;

	public interface ConfermaProtocollazionePraticaModulisticaHandler extends EventHandler {
		void onConfermaProtocollazionePraticaModulistica(ConfermaProtocollazionePraticaModulisticaEvent event);
	}

	public interface ConfermaProtocollazionePecInHasHandlers extends HasHandlers {
		HandlerRegistration addConfermaProtocollazionePecInHandler(ConfermaProtocollazionePraticaModulisticaHandler handler);
	}

	public ConfermaProtocollazionePraticaModulisticaEvent() {
	}

	@Override
	protected void dispatch(ConfermaProtocollazionePraticaModulisticaHandler handler) {
		handler.onConfermaProtocollazionePraticaModulistica(this);
	}

	@Override
	public Type<ConfermaProtocollazionePraticaModulisticaHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ConfermaProtocollazionePraticaModulisticaHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new ConfermaProtocollazionePraticaModulisticaEvent());
	}

	public CreaFascicoloDTO getCreaFascicoloDTO() {
		return creaFascicoloDTO;
	}

	public void setCreaFascicoloDTO(CreaFascicoloDTO creaFascicoloDTO) {
		this.creaFascicoloDTO = creaFascicoloDTO;
	}


	public String getIdPraticaModulistica() {
		return idPraticaModulistica;
	}

	public void setIdPraticaModulistica(String idPraticaModulistica) {
		this.idPraticaModulistica = idPraticaModulistica;
	}

	public DatiDefaultProtocollazione getDatiPerFormProtocollazione() {
		return datiPerFormProtocollazione;
	}

	public void setDatiPerFormProtocollazione(DatiDefaultProtocollazione datiPerFormProtocollazione) {
		this.datiPerFormProtocollazione = datiPerFormProtocollazione;
	}

}
