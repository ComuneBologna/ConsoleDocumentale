package com.google.gwt.user.client.ui;

  @Deprecated
  public interface FiresConsoleDisclosureEvents {

    /**
     * Adds a handler interface to receive open events.
     * 
     * @param handler the handler to add
     * @deprecated Add an open or close handler to the event source instead
     */
    @Deprecated
    void addEventHandler(ConsoleDisclosureHandler handler);

    /**
     * Removes a previously added handler interface.
     * 
     * @param handler the handler to remove
     * @deprecated Use the
     * {@link com.google.gwt.event.shared.HandlerRegistration#removeHandler}
     * method on the object returned by an add*Handler method instead
     */
    @Deprecated
    void removeEventHandler(ConsoleDisclosureHandler handler);
  }