package it.eng.portlet.consolepec.gwt.client.presenter.event.rubrica;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * @author GiacomoFM
 * @since 15/set/2017
 */
@AllArgsConstructor
public class MostraListaAnagraficheEvent extends GwtEvent<MostraListaAnagraficheEvent.MostraListaAnagraficheHandler> {

	private static Type<MostraListaAnagraficheHandler> TYPE = new Type<MostraListaAnagraficheHandler>();

	@Getter private Object openingRequestor;
	
	public interface MostraListaAnagraficheHandler extends EventHandler {
		void onMostraListaAnagrafiche(MostraListaAnagraficheEvent event);
	}

	@Override
	public Type<MostraListaAnagraficheHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraListaAnagraficheHandler> getType() {
		return TYPE;
	}

	@Override
	protected void dispatch(MostraListaAnagraficheHandler handler) {
		handler.onMostraListaAnagrafiche(this);
	}
}
