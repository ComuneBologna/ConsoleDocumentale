package it.eng.portlet.consolepec.gwt.client.widget;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;

/** Implementation of Google FastButton {@link http://code.google.com/mobile/articles/fast_buttons.html} */
public class FastButton extends Button {

	private boolean touchHandled = false;
	private boolean clickHandled = false;
	private boolean touchMoved = false;
	private int startY;
	private int startX;
	private int screenX;
	private int screenY;
	private int identifier;

	public FastButton() {
		// TODO - messages

		sinkEvents(Event.TOUCHEVENTS | Event.ONCLICK);
	}

	@Override
	public void onBrowserEvent(Event event) {
		switch (DOM.eventGetType(event)) {
		case Event.ONTOUCHSTART: {
			onTouchStart(event);
			break;
		}
		case Event.ONTOUCHEND: {
			onTouchEnd(event);
			break;
		}
		case Event.ONTOUCHMOVE: {
			onTouchMove(event);
			break;
		}
		case Event.ONCLICK: {
			onClick(event);
			return;
		}
		}

		super.onBrowserEvent(event);
	}

	private void onClick(Event event) {
		event.stopPropagation();
		// Window.alert("onclick "+touchHandled);
		if (touchHandled && !clickHandled) {
			/* gestione click in arrivo dal touch end */
			touchHandled = false;
			clickHandled = true;
			super.onBrowserEvent(event);
		} else {
			// if (clickHandled) {
			// clickHandled = false;
			/* gestione del click in arrivo da event click */
			// event.preventDefault();
			// } else {
			// super.onBrowserEvent(event);
			// }
		}
		this.setFocus(false);
	}

	private void onTouchEnd(Event event) {

		log("** Touchend ct=" + event.getChangedTouches().length() + " tt=" + event.getTargetTouches().length());
		/* controllo che sia stato rilasciato solo un dito */
		if (event.getChangedTouches().length() == 1 && !touchMoved && !clickHandled) {
			touchHandled = true;
			fireClick();
		}
	}

	private void onTouchMove(Event event) {
		// Window.alert("onTouchMove "+touchMoved);
		if (!touchMoved) {
			Touch touch = event.getTargetTouches().get(0);
			int deltaX = Math.abs(startX - touch.getClientX());
			int deltaY = Math.abs(startY - touch.getClientY());

			if (deltaX > 15 || deltaY > 15) {
				touchMoved = true;
			}
		}
	}

	private void onTouchStart(Event event) {
		event.stopPropagation();
		Touch touch = event.getTargetTouches().get(0);
		this.startX = touch.getClientX();
		this.startY = touch.getClientY();
		screenX = touch.getScreenX();
		screenY = touch.getScreenY();
		touchMoved = false;
		clickHandled = false;
		if (identifier == touch.getIdentifier()) {
			/* problema ipad quando vi è un popup (es browse file dialog) sopra al tasto, si è osservato che touch start è invocato più volte con lo stesso id, se l'utente esegue dei touch */
			touchMoved = true;
			return;
		}
		identifier = touch.getIdentifier();
		log("** Touchstart id=" + touch.getIdentifier() + " X=" + startX + " Y=" + startY);
	}

	private void fireClick() {
		NativeEvent evt = Document.get().createClickEvent(1, screenX, screenY, startX, startY, false, false, false, false);
		getElement().dispatchEvent(evt);
	}

	private void log(String str) {
		// String v = DOM.getElementById("outdebug").getInnerHTML();
		// DOM.getElementById("outdebug").setInnerHTML(v+" "+str);
	}

}