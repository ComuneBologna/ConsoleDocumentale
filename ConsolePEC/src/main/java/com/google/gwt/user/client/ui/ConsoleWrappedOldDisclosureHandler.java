package com.google.gwt.user.client.ui;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
  class ConsoleWrappedOldDisclosureHandler extends
	  ListenerWrapper<ConsoleDisclosureHandler> implements
	  CloseHandler<ConsoleDisclosurePanel>, OpenHandler<ConsoleDisclosurePanel> {
	
	public static void add(ConsoleDisclosurePanel source, ConsoleDisclosureHandler listener) {
		ConsoleWrappedOldDisclosureHandler handlers = new ConsoleWrappedOldDisclosureHandler(
	      listener);
	  source.addOpenHandler(handlers);
	  source.addCloseHandler(handlers);
	}
	
	public static void remove(Widget eventSource, ConsoleDisclosureHandler listener) {
		ListenerWrapper.baseRemove(eventSource, listener, CloseEvent.getType(),
	      OpenEvent.getType());
	}
	
	private ConsoleWrappedOldDisclosureHandler(ConsoleDisclosureHandler listener) {
	  super(listener);
	}
	
	public void onClose(CloseEvent<ConsoleDisclosurePanel> event) {
	  getListener().onClose(
	      new ConsoleDisclosureEvent((ConsoleDisclosurePanel) event.getSource()));
	}
	
	public void onOpen(OpenEvent<ConsoleDisclosurePanel> event) {
	  getListener().onOpen(
	      new ConsoleDisclosureEvent((ConsoleDisclosurePanel) event.getSource()));
	}
}