package it.eng.portlet.consolepec.gwt.client.presenter.event;

import java.util.Set;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class GoToAssegnaFromWorklistPraticaModulisticaEvent extends GwtEvent<GoToAssegnaFromWorklistPraticaModulisticaEvent.GoToAssegnaFromWorklistPraticaModulisticaHandler> {

	public static Type<GoToAssegnaFromWorklistPraticaModulisticaHandler> TYPE = new Type<GoToAssegnaFromWorklistPraticaModulisticaHandler>();

	public interface GoToAssegnaFromWorklistPraticaModulisticaHandler extends EventHandler {
		void onGoToAssegnaFromWorklistPraticaModulistica(GoToAssegnaFromWorklistPraticaModulisticaEvent event);
	}

	private Set<String> praticheModulistiche;

	public GoToAssegnaFromWorklistPraticaModulisticaEvent(Set<String> praticheModulistiche) {
		this.praticheModulistiche = praticheModulistiche;
	}

	@Override
	protected void dispatch(GoToAssegnaFromWorklistPraticaModulisticaHandler handler) {
		handler.onGoToAssegnaFromWorklistPraticaModulistica(this);
	}

	@Override
	public Type<GoToAssegnaFromWorklistPraticaModulisticaHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<GoToAssegnaFromWorklistPraticaModulisticaHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Set<String> praticheModulistiche) {
		source.fireEvent(new GoToAssegnaFromWorklistPraticaModulisticaEvent(praticheModulistiche));
	}

	public Set<String> getPraticheModulistiche() {
		return praticheModulistiche;
	}

}
