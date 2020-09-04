package it.eng.portlet.consolepec.gwt.client.presenter.event;

import java.util.Set;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class GoToAssegnaFromWorklistFascicoloEvent extends GwtEvent<GoToAssegnaFromWorklistFascicoloEvent.GoToAssegnaFromWorklistFascicoloHandler> {

	public static Type<GoToAssegnaFromWorklistFascicoloHandler> TYPE = new Type<GoToAssegnaFromWorklistFascicoloHandler>();

	private String identificativoWorklist;
	private Set<String> ids;

	public interface GoToAssegnaFromWorklistFascicoloHandler extends EventHandler {
		void onGoToAssegnaFromWorklistFascicolo(GoToAssegnaFromWorklistFascicoloEvent event);
	}

	public GoToAssegnaFromWorklistFascicoloEvent(Set<String> ids, String identificativoWorklist) {
		this.ids = ids;
		this.identificativoWorklist = identificativoWorklist;
	}

	public Set<String> getIds() {
		return ids;
	}

	@Override
	protected void dispatch(GoToAssegnaFromWorklistFascicoloHandler handler) {
		handler.onGoToAssegnaFromWorklistFascicolo(this);
	}

	@Override
	public Type<GoToAssegnaFromWorklistFascicoloHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<GoToAssegnaFromWorklistFascicoloHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Set<String> ids, String identificativoWorklist) {
		source.fireEvent(new GoToAssegnaFromWorklistFascicoloEvent(ids,identificativoWorklist));
	}

	public String getIdentificativoWorklist() {
		return identificativoWorklist;
	}



}
