package it.eng.portlet.consolepec.gwt.client.event.collegamento;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ChiudiCollegaFascicoloConRicercaEvent extends GwtEvent<ChiudiCollegaFascicoloConRicercaEvent.ChiudiCollegaFascicoloConRicercaHandler> {

	public static Type<ChiudiCollegaFascicoloConRicercaHandler> TYPE = new Type<ChiudiCollegaFascicoloConRicercaHandler>();

	public interface ChiudiCollegaFascicoloConRicercaHandler extends EventHandler {
		void onChiudiCollegaFascicolo(ChiudiCollegaFascicoloConRicercaEvent event);
	}

	private AzioneCollegamento azioneCollegamento;

	public ChiudiCollegaFascicoloConRicercaEvent(AzioneCollegamento azioneCollegamento) {
		this.azioneCollegamento = azioneCollegamento;
	}

	@Override
	protected void dispatch(ChiudiCollegaFascicoloConRicercaHandler handler) {
		handler.onChiudiCollegaFascicolo(this);
	}

	@Override
	public Type<ChiudiCollegaFascicoloConRicercaHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ChiudiCollegaFascicoloConRicercaHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, AzioneCollegamento azioneCollegamento) {
		source.fireEvent(new ChiudiCollegaFascicoloConRicercaEvent(azioneCollegamento));
	}

	public AzioneCollegamento getAzioneCollegamento() {
		return azioneCollegamento;
	}

}
