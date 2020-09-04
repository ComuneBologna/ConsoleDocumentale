package it.eng.portlet.consolepec.gwt.client.presenter.event;

import java.util.Set;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import it.eng.portlet.consolepec.gwt.client.presenter.event.MostraModificaTipologieAllegatoEvent.MostraModificaTipologieAllegatoHandler;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author GiacomoFM
 * @since 10/dic/2018
 */
@AllArgsConstructor
public class MostraModificaTipologieAllegatoEvent extends GwtEvent<MostraModificaTipologieAllegatoHandler> {

	@Getter private static Type<MostraModificaTipologieAllegatoHandler> type = new Type<MostraModificaTipologieAllegatoHandler>();

	public interface MostraModificaTipologieAllegatoHandler extends EventHandler {
		void onMostraModificaTipologiaAllegato(MostraModificaTipologieAllegatoEvent event);
	}

	@Getter private String pathFascicolo;
	@Getter private Set<AllegatoDTO> allegati;

	@Override
	public Type<MostraModificaTipologieAllegatoHandler> getAssociatedType() {
		return type;
	}

	@Override
	protected void dispatch(MostraModificaTipologieAllegatoHandler handler) {
		handler.onMostraModificaTipologiaAllegato(this);
	}

}
