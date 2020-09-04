package it.eng.portlet.consolepec.gwt.client.presenter.event;

import java.util.Set;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class GoToAssegnaFromWorklistPecInEvent extends GwtEvent<GoToAssegnaFromWorklistPecInEvent.GoToAssegnaFromWorklistPecInHandler> {

	public static Type<GoToAssegnaFromWorklistPecInHandler> TYPE = new Type<GoToAssegnaFromWorklistPecInHandler>();

	public interface GoToAssegnaFromWorklistPecInHandler extends EventHandler {
		void onGoToAssegnaFromWorklistPecIn(GoToAssegnaFromWorklistPecInEvent event);
	}

	@Getter
	private Set<String> pecinid;

	@Getter
	private String identificativoWorklist;

	@Override
	protected void dispatch(GoToAssegnaFromWorklistPecInHandler handler) {
		handler.onGoToAssegnaFromWorklistPecIn(this);
	}

	@Override
	public Type<GoToAssegnaFromWorklistPecInHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<GoToAssegnaFromWorklistPecInHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Set<String> pecinid, String identificativoWorklist) {
		source.fireEvent(new GoToAssegnaFromWorklistPecInEvent(pecinid, identificativoWorklist));
	}
}
