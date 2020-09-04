package it.eng.portlet.consolepec.gwt.client.command;

import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;

import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

public interface ConsolePecCommandBinder extends HasHandlers{
	
	public EventBus _getEventBus();

	public DispatchAsync getDispatchAsync();

	public PlaceManager getPlaceManager();

	public PecInPraticheDB getPecInPraticheDB();

}
