package com.google.gwt.user.client.ui;

import java.util.EventListener;


/**
 * Event handler interface for {@link DisclosureEvent}.
 * 
 * @deprecated Use {@link com.google.gwt.event.logical.shared.CloseHandler}
 *             and/or {@link com.google.gwt.event.logical.shared.OpenHandler}
 *             instead
 * @see DisclosurePanel
 */
@Deprecated
public interface ConsoleDisclosureHandler extends EventListener {
	/**
	 * Fired when the panel is closed.
	 * 
	 * @param event
	 *            event representing this action.
	 * @deprecated Use {@link com.google.gwt.event.logical.shared.CloseHandler}
	 *             instead
	 */
	@Deprecated
	void onClose(ConsoleDisclosureEvent event);

	/**
	 * Fired when the panel is opened.
	 * 
	 * @param event
	 *            event representing this action.
	 * @deprecated Use {@link com.google.gwt.event.logical.shared.OpenHandler}
	 *             instead
	 */
	@Deprecated
	void onOpen(ConsoleDisclosureEvent event);
}