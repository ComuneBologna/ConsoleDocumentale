package it.eng.portlet.consolepec.gwt.client.presenter.pec.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MostraWorklistPecInEvent extends GwtEvent<MostraWorklistPecInEvent.MostraWorklistPecInHandler> {

	public static Type<MostraWorklistPecInHandler> TYPE = new Type<MostraWorklistPecInHandler>();

	public interface MostraWorklistPecInHandler extends EventHandler {
		void onMostraWorklistPecInHandler(MostraWorklistPecInEvent event);
	}

	@Getter
	private String identificativoWorklist;

	@Override
	protected void dispatch(MostraWorklistPecInHandler handler) {
		handler.onMostraWorklistPecInHandler(this);
	}

	@Override
	public Type<MostraWorklistPecInHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraWorklistPecInHandler> getType() {
		return TYPE;
	}
}
