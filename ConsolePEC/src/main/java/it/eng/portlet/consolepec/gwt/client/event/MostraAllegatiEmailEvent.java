package it.eng.portlet.consolepec.gwt.client.event;

import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class MostraAllegatiEmailEvent extends GwtEvent<MostraAllegatiEmailEvent.MostraAllegatiEmailHandler> {

	public static Type<MostraAllegatiEmailHandler> TYPE = new Type<MostraAllegatiEmailHandler>();
	private List<PecInDTO> listaMail = new ArrayList<PecInDTO>();
	private String fascicoloPath;

	public interface MostraAllegatiEmailHandler extends EventHandler {
		void onMostraAllegatiEmail(MostraAllegatiEmailEvent event);
	}

	public MostraAllegatiEmailEvent(List<PecInDTO> listaMail, String fascicoloPath) {
		this.listaMail.addAll(listaMail);
		this.fascicoloPath = fascicoloPath;
	}

	public List<PecInDTO> getListaMail() {
		return listaMail;
	}
	
	public String getFascicoloPath() {
		return fascicoloPath;
	}

	@Override
	protected void dispatch(MostraAllegatiEmailHandler handler) {
		handler.onMostraAllegatiEmail(this);
	}

	@Override
	public Type<MostraAllegatiEmailHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraAllegatiEmailHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, List<PecInDTO> listaMail, String fascicoloPath) {
		source.fireEvent(new MostraAllegatiEmailEvent(listaMail, fascicoloPath));
	}
}
