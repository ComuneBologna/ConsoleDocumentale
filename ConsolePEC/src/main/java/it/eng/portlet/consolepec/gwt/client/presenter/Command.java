package it.eng.portlet.consolepec.gwt.client.presenter;


public interface Command<U, T> {
	public U exe(T t);
}
