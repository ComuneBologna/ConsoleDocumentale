package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MostraWorklistFascicoliEvent extends GwtEvent<MostraWorklistFascicoliEvent.MostraWorklistFascicoliHandler> {

	@Getter
	private String identificativoWorklist;

	public static Type<MostraWorklistFascicoliHandler> TYPE = new Type<MostraWorklistFascicoliHandler>();

	public interface MostraWorklistFascicoliHandler extends EventHandler {
		void onMostraWorklistFascicoli(MostraWorklistFascicoliEvent event);
	}

	@Override
	protected void dispatch(MostraWorklistFascicoliHandler handler) {
		handler.onMostraWorklistFascicoli(this);
	}

	@Override
	public Type<MostraWorklistFascicoliHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraWorklistFascicoliHandler> getType() {
		return TYPE;
	}
}
