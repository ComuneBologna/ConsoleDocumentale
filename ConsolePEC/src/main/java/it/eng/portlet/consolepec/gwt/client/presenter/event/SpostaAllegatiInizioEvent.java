package it.eng.portlet.consolepec.gwt.client.presenter.event;

import java.util.Set;
import java.util.TreeSet;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpostaAllegatiInizioEvent extends GwtEvent<SpostaAllegatiInizioEvent.SpostaAllegatiEventHandler> {

	public static Type<SpostaAllegatiEventHandler> TYPE = new Type<SpostaAllegatiEventHandler>();

	@Getter
	private String fascicoloSorgente;

	@Getter
	private Set<AllegatoDTO> allegatiDaSpostare = new TreeSet<AllegatoDTO>();

	public interface SpostaAllegatiEventHandler extends EventHandler {
		void onSpostaAllegati(SpostaAllegatiInizioEvent event);
	}

	@Override
	protected void dispatch(SpostaAllegatiEventHandler handler) {
		handler.onSpostaAllegati(this);
	}

	@Override
	public Type<SpostaAllegatiEventHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<SpostaAllegatiEventHandler> getType() {
		return TYPE;
	}

}
