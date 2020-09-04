package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command.protocollazione;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.SceltaCapofilaPresenter;
import it.eng.portlet.consolepec.gwt.shared.dto.DatiPg;

public abstract class AbstractDatiProtocollazioneCommand extends AbstractConsolePecCommand<SceltaCapofilaPresenter> {
	
	public AbstractDatiProtocollazioneCommand(SceltaCapofilaPresenter presenter) {
		super(presenter);
	}

	public abstract DatiPg getDatiPg();

	public abstract void setDatiPg(DatiPg datiPg);

}
