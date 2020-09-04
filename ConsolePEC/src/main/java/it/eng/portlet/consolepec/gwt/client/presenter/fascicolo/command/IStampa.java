package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import it.eng.portlet.consolepec.gwt.client.command.ConsolePecCommandBinder;

import com.google.gwt.safehtml.shared.SafeUri;

public interface IStampa extends ConsolePecCommandBinder{
	
	public void downloadStampa(SafeUri uri);

}
