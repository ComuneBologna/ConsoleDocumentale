package it.eng.portlet.consolepec.gwt.client.command;

import com.google.gwt.user.client.Command;

public abstract class AbstractConsolePecCommand<T extends ConsolePecCommandBinder> implements Command {

	private T presenter;

	public AbstractConsolePecCommand(T presenter) {
		super();
		this.presenter = presenter;
	}

	protected T getPresenter() {
		return presenter;
	}

	@Override
	public void execute() {
		if (this.presenter == null)
			throw new IllegalStateException("Presenter cannot be null in the command");
		this._execute();
	}

	protected abstract void _execute();

}
