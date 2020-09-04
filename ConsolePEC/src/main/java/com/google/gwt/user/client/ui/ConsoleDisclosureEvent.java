package com.google.gwt.user.client.ui;

import java.util.EventObject;
  @Deprecated
  public class ConsoleDisclosureEvent extends EventObject {
    /**
     * Creates a new instance of the event object.
     * 
     * @param sender the panel from which the event is originating.
     * 
     * @see DisclosureHandler
     * @deprecated Use
     *             {@link com.google.gwt.event.logical.shared.CloseEvent} and
     *             {@link  com.google.gwt.event.logical.shared.OpenEvent} instead
     */
    @Deprecated
    public ConsoleDisclosureEvent(ConsoleDisclosurePanel sender) {
      super(sender);
    }
  }