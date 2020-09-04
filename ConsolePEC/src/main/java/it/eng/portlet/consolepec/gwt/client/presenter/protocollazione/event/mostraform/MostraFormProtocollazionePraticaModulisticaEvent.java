package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.mostraform;

import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CreaFascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.DatiDefaultProtocollazione;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class MostraFormProtocollazionePraticaModulisticaEvent extends GwtEvent<MostraFormProtocollazionePraticaModulisticaEvent.MostraFormProtocollazionePraticaModulisticaHandler> {

	public static Type<MostraFormProtocollazionePraticaModulisticaHandler> TYPE = new Type<MostraFormProtocollazionePraticaModulisticaHandler>();

	private DatiDefaultProtocollazione datiDefaultProtocollazione;
	private CreaFascicoloDTO creaFascicoloDTO;
	private PraticaModulisticaDTO praticaModulisticaDTO;

	public PraticaModulisticaDTO getPraticaModulisticaDTO() {
		return praticaModulisticaDTO;
	}

	public void setPraticaModulisticaDTO(PraticaModulisticaDTO praticaModulisticaDTO) {
		this.praticaModulisticaDTO = praticaModulisticaDTO;
	}

	public interface MostraFormProtocollazionePraticaModulisticaHandler extends EventHandler {
		void onMostraFormProtocollazionePraticaModulistica(MostraFormProtocollazionePraticaModulisticaEvent event);
	}

	public MostraFormProtocollazionePraticaModulisticaEvent() {
	}

	@Override
	protected void dispatch(MostraFormProtocollazionePraticaModulisticaHandler handler) {
		handler.onMostraFormProtocollazionePraticaModulistica(this);
	}

	@Override
	public Type<MostraFormProtocollazionePraticaModulisticaHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraFormProtocollazionePraticaModulisticaHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new MostraFormProtocollazionePraticaModulisticaEvent());
	}

	public DatiDefaultProtocollazione getDatiDefaultProtocollazione() {
		return datiDefaultProtocollazione;
	}

	public void setDatiDefaultProtocollazione(DatiDefaultProtocollazione datiDefaultProtocollazione) {
		this.datiDefaultProtocollazione = datiDefaultProtocollazione;
	}

	public CreaFascicoloDTO getCreaFascicoloDTO() {
		return creaFascicoloDTO;
	}

	public void setCreaFascicoloDTO(CreaFascicoloDTO creaFascicoloDTO) {
		this.creaFascicoloDTO = creaFascicoloDTO;
	}

}
