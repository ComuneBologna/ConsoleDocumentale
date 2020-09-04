package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.urbanistica.DettaglioPraticaProcediPresenter;
import it.eng.portlet.consolepec.gwt.shared.UriMapping;

import com.google.gwt.safehtml.shared.SafeUri;

public class DownloadAllegatoProcediCommand extends AbstractConsolePecCommand<DettaglioPraticaProcediPresenter> {

	private String uidAlfresco;
	private DettaglioPraticaProcediPresenter presenter;
	
	public DownloadAllegatoProcediCommand(DettaglioPraticaProcediPresenter presenter, String uidAlfresco) {
		super(presenter);
		this.presenter = presenter;
		this.uidAlfresco = uidAlfresco;
	}

	@Override
	protected void _execute() {
		
		SafeUri uri = UriMapping.generaDownloadAllegatoServletURL(uidAlfresco);
		
		presenter.getView().sendDownload(uri);
	}
}
