package it.eng.portlet.consolepec.gwt.client.presenter;

public interface BiCommand<U, T, W> {
	
	public U exe(T t, W w);
}
