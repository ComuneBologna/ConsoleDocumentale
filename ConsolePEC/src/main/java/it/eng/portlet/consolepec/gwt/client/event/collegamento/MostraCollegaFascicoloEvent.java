package it.eng.portlet.consolepec.gwt.client.event.collegamento;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class MostraCollegaFascicoloEvent extends GwtEvent<MostraCollegaFascicoloEvent.MostraCollegaFascicoloHandler> {

	public enum TipoMostraCollegaFascicolo {
		DIRETTO, CON_RICERCA
	}

	private String fascicoloOriginarioPath;
	private String fascicoloDaCollegarePath;
	private TipoMostraCollegaFascicolo tipoMostraCollegaFascicolo;

	public static Type<MostraCollegaFascicoloHandler> TYPE = new Type<MostraCollegaFascicoloHandler>();

	public interface MostraCollegaFascicoloHandler extends EventHandler {
		void onMostraCollegaFascicolo(MostraCollegaFascicoloEvent event);
	}

	/**
	 * 
	 * @param fascicoloOriginarioPath
	 * @param fascicoloDaCollegarePath
	 */
	public MostraCollegaFascicoloEvent(String fascicoloOriginarioPath, String fascicoloDaCollegarePath, TipoMostraCollegaFascicolo tipoMostraCollegaFascicolo) {
		this.fascicoloDaCollegarePath = fascicoloDaCollegarePath;
		this.fascicoloOriginarioPath = fascicoloOriginarioPath;
		this.tipoMostraCollegaFascicolo = tipoMostraCollegaFascicolo;
	}

	@Override
	protected void dispatch(MostraCollegaFascicoloHandler handler) {
		handler.onMostraCollegaFascicolo(this);
	}

	@Override
	public Type<MostraCollegaFascicoloHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraCollegaFascicoloHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String fascicoloOriginarioPath, String fascicoloDaCollegarePath, TipoMostraCollegaFascicolo tipoMostraFascicolo) {
		source.fireEvent(new MostraCollegaFascicoloEvent(fascicoloOriginarioPath, fascicoloDaCollegarePath, tipoMostraFascicolo));
	}

	public String getFascicoloOriginarioPath() {
		return fascicoloOriginarioPath;
	}

	public String getFascicoloDaCollegarePath() {
		return fascicoloDaCollegarePath;
	}

	public TipoMostraCollegaFascicolo getTipoMostraCollegaFascicolo() {
		return tipoMostraCollegaFascicolo;
	}

}
