package it.eng.portlet.consolepec.gwt.client.presenter;

import it.eng.portlet.consolepec.gwt.client.event.UpdateSiteMapEvent;
import it.eng.portlet.consolepec.gwt.client.worklist.AbstractWorklistStrategy;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;

import java.util.Set;

import javax.validation.ConstraintViolation;

import com.google.gwt.user.client.Command;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;

/* Ricerca arriva dall'utente */
public class RicercaCommand implements Command {

	private Set<ConstraintViolation<CercaPratiche>> violations;
	private AbstractWorklistStrategy strategy;
	private EventBus eventBus;

	public RicercaCommand(EventBus eventBus, AbstractWorklistStrategy strategy, Set<ConstraintViolation<CercaPratiche>> violations) {
		this.strategy = strategy;
		this.violations = violations;
		this.eventBus = eventBus;
	}

	@Override
	@ProxyEvent
	public void execute() {
		if (violations.size() == 0) {
			strategy.restartSearchDatiGrid();
			eventBus.fireEvent(new UpdateSiteMapEvent());
		}
	}
	
	public void refreshDatiGrid() {
		if (violations.size() == 0) {
			strategy.refreshDatiGrid();
			eventBus.fireEvent(new UpdateSiteMapEvent());
		}
	}
}
