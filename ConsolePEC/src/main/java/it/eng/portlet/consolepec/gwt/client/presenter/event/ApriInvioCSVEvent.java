package it.eng.portlet.consolepec.gwt.client.presenter.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import it.eng.portlet.consolepec.gwt.client.presenter.event.ApriInvioCSVEvent.ApriInvioCSVHandler;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApriInvioCSVEvent extends GwtEvent<ApriInvioCSVHandler> {

	@Getter
	private static Type<ApriInvioCSVHandler> type = new Type<ApriInvioCSVHandler>();

	public interface ApriInvioCSVHandler extends EventHandler {
		void onApriInvioCsv(ApriInvioCSVEvent event);
	}

	@Getter
	private String clientIdFascicolo;

	@Getter
	private String nomeAllegato;

	@Override
	public Type<ApriInvioCSVHandler> getAssociatedType() {
		return type;
	}

	@Override
	protected void dispatch(ApriInvioCSVHandler handler) {
		handler.onApriInvioCsv(this);
	}
}
