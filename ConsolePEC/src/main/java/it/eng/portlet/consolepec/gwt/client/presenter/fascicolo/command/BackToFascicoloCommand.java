package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.event.BackToFascicoloEvent;


public class BackToFascicoloCommand <T extends IBackToFascicolo> extends AbstractConsolePecCommand<T> {

	public BackToFascicoloCommand(T presenter) {
		super(presenter);
	}

	@Override
	protected void _execute() {
		getPresenter()._getEventBus().fireEvent(new BackToFascicoloEvent(getPresenter().getFascicoloPath()));
	}

}
