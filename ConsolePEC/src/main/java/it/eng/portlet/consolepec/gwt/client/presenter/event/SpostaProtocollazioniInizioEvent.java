package it.eng.portlet.consolepec.gwt.client.presenter.event;

import java.util.Set;
import java.util.TreeSet;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoElenco;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpostaProtocollazioniInizioEvent extends GwtEvent<SpostaProtocollazioniInizioEvent.SpostaProtocollazioniEventHandler> {

	public static Type<SpostaProtocollazioniEventHandler> TYPE = new Type<SpostaProtocollazioniEventHandler>();

	@Getter
	private String fascicoloSorgente;

	@Getter
	private Set<AllegatoDTO> allegatiProtocollati = new TreeSet<AllegatoDTO>();

	@Getter
	private Set<ElementoElenco> praticheProtocollate = new TreeSet<ElementoElenco>();

	public interface SpostaProtocollazioniEventHandler extends EventHandler {
		void onSpostaAllegati(SpostaProtocollazioniInizioEvent event);
	}

	@Override
	protected void dispatch(SpostaProtocollazioniEventHandler handler) {
		handler.onSpostaAllegati(this);
	}

	@Override
	public Type<SpostaProtocollazioniEventHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<SpostaProtocollazioniEventHandler> getType() {
		return TYPE;
	}

}
