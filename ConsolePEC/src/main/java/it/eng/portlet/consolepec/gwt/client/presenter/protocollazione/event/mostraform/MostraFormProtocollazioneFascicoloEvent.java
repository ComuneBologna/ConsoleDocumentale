package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.mostraform;

import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.DatiDefaultProtocollazione;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

import java.util.TreeSet;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public class MostraFormProtocollazioneFascicoloEvent extends GwtEvent<MostraFormProtocollazioneFascicoloEvent.MostraFormProtocollazioneFascicoloHandler> {

	public static Type<MostraFormProtocollazioneFascicoloHandler> TYPE = new Type<MostraFormProtocollazioneFascicoloHandler>();

	private FascicoloDTO fascicoloDTO;
	private TreeSet<PraticaDTO> praticheDTO = new TreeSet<PraticaDTO>();
	private TreeSet<AllegatoDTO> allegatiDTO = new TreeSet<AllegatoDTO>();
	private DatiDefaultProtocollazione datiDefaultProtocollazione;
	private String tipoProtocollazione;

	public interface MostraFormProtocollazioneFascicoloHandler extends EventHandler {
		void onMostraFormProtocollazioneFascicolo(MostraFormProtocollazioneFascicoloEvent event);
	}

	public interface MostraFormProtocollazioneFascicoloHasHandlers extends HasHandlers {
		HandlerRegistration addMostraFormProtocollazioneFascicoloHandler(MostraFormProtocollazioneFascicoloHandler handler);
	}

	public MostraFormProtocollazioneFascicoloEvent() {
	}

	@Override
	protected void dispatch(MostraFormProtocollazioneFascicoloHandler handler) {
		handler.onMostraFormProtocollazioneFascicolo(this);
	}

	@Override
	public Type<MostraFormProtocollazioneFascicoloHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraFormProtocollazioneFascicoloHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new MostraFormProtocollazioneFascicoloEvent());
	}

	public FascicoloDTO getFascicoloDTO() {
		return fascicoloDTO;
	}

	public void setFascicoloDTO(FascicoloDTO fascicoloDTO) {
		this.fascicoloDTO = fascicoloDTO;
	}

	public TreeSet<PraticaDTO> getPraticheDTO() {
		return praticheDTO;
	}

	public void setPraticheDTO(TreeSet<PraticaDTO> praticheDTO) {
		this.praticheDTO = praticheDTO;
	}

	public TreeSet<AllegatoDTO> getAllegatiDTO() {
		return allegatiDTO;
	}

	public void setAllegatiDTO(TreeSet<AllegatoDTO> allegatiDTO) {
		this.allegatiDTO = allegatiDTO;
	}

	public DatiDefaultProtocollazione getDatiDefaultProtocollazione() {
		return datiDefaultProtocollazione;
	}

	public void setDatiDefaultProtocollazione(DatiDefaultProtocollazione datiDefaultProtocollazione) {
		this.datiDefaultProtocollazione = datiDefaultProtocollazione;
	}

	public String getTipoProtocollazione() {
		return tipoProtocollazione;
	}

	public void setTipoProtocollazione(String tipoProtocollazione) {
		this.tipoProtocollazione = tipoProtocollazione;
	}

}
